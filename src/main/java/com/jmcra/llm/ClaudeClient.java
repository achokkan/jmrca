package com.jmcra.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmcra.config.JmcraProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

/**
 * Anthropic Claude client (Haiku for Tier 1, Sonnet for Tier 2).
 *
 * <h3>API Details</h3>
 * Uses the Anthropic Messages API v1 ({@code /v1/messages}).
 * Models:
 * <ul>
 *   <li>Tier 1: {@code claude-haiku-3-5} — &lt;2s latency budget</li>
 *   <li>Tier 2: {@code claude-sonnet-4-5} — &lt;15s latency budget</li>
 * </ul>
 *
 * Spec: Section 6.1 (Model Selection Rationale).
 */
public class ClaudeClient implements LlmClient {

  private static final Logger log = LoggerFactory.getLogger(ClaudeClient.class);

  private static final String TIER1_MODEL = "claude-haiku-3-5";
  private static final String TIER2_MODEL = "claude-sonnet-4-6";
  private static final String API_BASE    = "https://api.anthropic.com";
  private static final String API_VERSION = "2023-06-01";

  private final WebClient    webClient;
  private final ObjectMapper mapper;
  private final JmcraProperties props;
  private final boolean tier2;

  public ClaudeClient(WebClient.Builder webClientBuilder,
                      ObjectMapper objectMapper,
                      JmcraProperties props,
                      boolean tier2) {
    this.props     = props;
    this.mapper    = objectMapper;
    this.tier2     = tier2;
    this.webClient = webClientBuilder
        .baseUrl(API_BASE)
        .defaultHeader("x-api-key",        props.llm().claude().apiKey())
        .defaultHeader("anthropic-version", API_VERSION)
        .defaultHeader("content-type",      "application/json")
        .build();
  }

  @Override
  public Mono<LlmResponse> chat(LlmRequest request) {
    String model = tier2 ? TIER2_MODEL : TIER1_MODEL;
    var body = Map.of(
        "model",      model,
        "max_tokens", request.maxTokens(),
        "system",     request.systemPrompt(),
        "messages",   new Object[]{Map.of("role", "user", "content", request.userPrompt())}
    );

    return webClient.post()
        .uri("/v1/messages")
        .bodyValue(body)
        .retrieve()
        .bodyToMono(String.class)
        .flatMap(json -> parseResponse(json, model))
        .timeout(Duration.ofSeconds(tier2 ? 30 : 5))
        .retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
            .filter(t -> t instanceof IllegalArgumentException))
        .doOnNext(r -> log.debug("[{}] Claude {} tokens used: {}", request.ruleId(),
            model, r.totalTokens()))
        .doOnError(e -> log.error("[{}] Claude call failed: {}", request.ruleId(),
            e.getMessage()));
  }

  private Mono<LlmResponse> parseResponse(String json, String model) {
    try {
      JsonNode root           = mapper.readTree(json);
      String   content        = root.at("/content/0/text").asText();
      int      promptTokens   = root.at("/usage/input_tokens").asInt();
      int      completionTokens = root.at("/usage/output_tokens").asInt();
      return Mono.just(new LlmResponse(content, model, promptTokens, completionTokens, 0));
    } catch (Exception e) {
      return Mono.error(new IllegalArgumentException("Malformed Claude response: " + e.getMessage(), e));
    }
  }

  @Override public boolean     isTier2()  { return tier2; }
  @Override public LlmProvider provider() { return LlmProvider.CLAUDE; }
}
