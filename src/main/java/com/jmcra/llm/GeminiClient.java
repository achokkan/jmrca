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
 * Google Gemini client (Gemini 2.0 Flash for Tier 1, Gemini 2.0 Pro for Tier 2).
 *
 * <h3>API Details</h3>
 * Uses the Gemini API ({@code /v1beta/models/{model}:generateContent}).
 * Models:
 * <ul>
 *   <li>Tier 1: {@code gemini-2.0-flash} — &lt;2s latency budget</li>
 *   <li>Tier 2: {@code gemini-2.0-pro-exp-02-05} — &lt;15s latency budget</li>
 * </ul>
 *
 * Spec: Section 6.1 (Model Selection Rationale).
 */
public class GeminiClient implements LlmClient {

  private static final Logger log = LoggerFactory.getLogger(GeminiClient.class);

  private static final String TIER1_MODEL = "gemini-2.0-flash";
  private static final String TIER2_MODEL = "gemini-2.0-pro-exp-02-05";
  private static final String API_BASE    = "https://generativelanguage.googleapis.com";

  private final WebClient    webClient;
  private final ObjectMapper mapper;
  private final JmcraProperties props;
  private final boolean      tier2;

  public GeminiClient(WebClient.Builder webClientBuilder,
                      ObjectMapper objectMapper,
                      JmcraProperties props,
                      boolean tier2) {
    this.props  = props;
    this.mapper = objectMapper;
    this.tier2  = tier2;
    this.webClient = webClientBuilder.baseUrl(API_BASE).build();
  }

  @Override
  public Mono<LlmResponse> chat(LlmRequest request) {
    String model = tier2 ? TIER2_MODEL : TIER1_MODEL;
    String uri   = "/v1beta/models/" + model + ":generateContent?key=" +
                   props.llm().gemini().apiKey();

    var body = Map.of(
        "system_instruction", Map.of("parts", List.of(Map.of("text", request.systemPrompt()))),
        "contents", List.of(Map.of("parts", List.of(Map.of("text", request.userPrompt()))))
    );

    return webClient.post()
        .uri(uri)
        .bodyValue(body)
        .retrieve()
        .bodyToMono(String.class)
        .flatMap(json -> parseResponse(json, model))
        .timeout(Duration.ofSeconds(tier2 ? 30 : 5))
        .retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
            .filter(t -> t instanceof IllegalArgumentException))
        .doOnNext(r -> log.debug("[{}] Gemini {} tokens used: {}", request.ruleId(),
            model, r.totalTokens()))
        .doOnError(e -> log.error("[{}] Gemini call failed: {}", request.ruleId(),
            e.getMessage()));
  }

  private Mono<LlmResponse> parseResponse(String json, String model) {
    try {
      JsonNode root    = mapper.readTree(json);
      String   content = root.at("/candidates/0/content/parts/0/text").asText();
      int      inTok   = root.at("/usageMetadata/promptTokenCount").asInt();
      int      outTok  = root.at("/usageMetadata/candidatesTokenCount").asInt();
      return Mono.just(new LlmResponse(content, model, inTok, outTok, 0));
    } catch (Exception e) {
      return Mono.error(new IllegalArgumentException("Malformed Gemini response: " + e.getMessage(), e));
    }
  }

  @Override public boolean     isTier2()  { return tier2; }
  @Override public LlmProvider provider() { return LlmProvider.GEMINI; }
}
