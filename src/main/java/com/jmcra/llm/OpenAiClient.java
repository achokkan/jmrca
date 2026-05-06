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
import java.util.List;
import java.util.Map;

/**
 * OpenAI ChatCompletion client (GPT-4o-mini for Tier 1, GPT-4o for Tier 2).
 *
 * <h3>API Details</h3>
 * Uses the OpenAI Chat Completions API ({@code /v1/chat/completions}).
 * Models:
 * <ul>
 *   <li>Tier 1: {@code gpt-4o-mini} — &lt;2s latency budget</li>
 *   <li>Tier 2: {@code gpt-4o} — &lt;15s latency budget</li>
 * </ul>
 *
 * Spec: Section 6.1 (Model Selection Rationale).
 */
public class OpenAiClient implements LlmClient {

  private static final Logger log = LoggerFactory.getLogger(OpenAiClient.class);

  private static final String TIER1_MODEL = "gpt-4o-mini";
  private static final String TIER2_MODEL = "gpt-4o";
  private static final String API_BASE    = "https://api.openai.com";

  private final WebClient    webClient;
  private final ObjectMapper mapper;
  private final boolean      tier2;

  public OpenAiClient(WebClient.Builder webClientBuilder,
                      ObjectMapper objectMapper,
                      JmcraProperties props,
                      boolean tier2) {
    this.mapper = objectMapper;
    this.tier2  = tier2;
    this.webClient = webClientBuilder
        .baseUrl(API_BASE)
        .defaultHeader("Authorization", "Bearer " + props.llm().openai().apiKey())
        .defaultHeader("Content-Type",  "application/json")
        .build();
  }

  @Override
  public Mono<LlmResponse> chat(LlmRequest request) {
    String model = tier2 ? TIER2_MODEL : TIER1_MODEL;
    var body = Map.of(
        "model",       model,
        "max_tokens",  request.maxTokens(),
        "messages", List.of(
            Map.of("role", "system",  "content", request.systemPrompt()),
            Map.of("role", "user",    "content", request.userPrompt())
        )
    );

    return webClient.post()
        .uri("/v1/chat/completions")
        .bodyValue(body)
        .retrieve()
        .bodyToMono(String.class)
        .flatMap(json -> parseResponse(json, model))
        .timeout(Duration.ofSeconds(tier2 ? 30 : 5))
        .retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
            .filter(t -> t instanceof IllegalArgumentException))
        .doOnNext(r -> log.debug("[{}] OpenAI {} tokens used: {}", request.ruleId(),
            model, r.totalTokens()))
        .doOnError(e -> log.error("[{}] OpenAI call failed: {}", request.ruleId(),
            e.getMessage()));
  }

  private Mono<LlmResponse> parseResponse(String json, String model) {
    try {
      JsonNode root           = mapper.readTree(json);
      String   content        = root.at("/choices/0/message/content").asText();
      int      promptTokens   = root.at("/usage/prompt_tokens").asInt();
      int      completionTokens = root.at("/usage/completion_tokens").asInt();
      return Mono.just(new LlmResponse(content, model, promptTokens, completionTokens, 0));
    } catch (Exception e) {
      return Mono.error(new IllegalArgumentException("Malformed OpenAI response: " + e.getMessage(), e));
    }
  }

  @Override public boolean     isTier2()  { return tier2; }
  @Override public LlmProvider provider() { return LlmProvider.OPENAI; }
}
