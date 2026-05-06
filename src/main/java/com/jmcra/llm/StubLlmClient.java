package com.jmcra.llm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Local Stub implementation of {@link LlmClient}.
 * Returns mocked responses to avoid external API calls during testing or
 * when network restrictions (Zscaler) are present.
 */
public class StubLlmClient implements LlmClient {

  private static final Logger log = LoggerFactory.getLogger(StubLlmClient.class);
  private final boolean tier2;

  public StubLlmClient(boolean tier2) {
    this.tier2 = tier2;
  }

  @Override
  public Mono<LlmResponse> chat(LlmRequest request) {
    String model = tier2 ? "stub-deep-model" : "stub-fast-model";
    log.info("[{}] Stub LLM processing request for rule {} (Tier {})",
        request.ruleId(), request.ruleId(), tier2 ? 2 : 1);

    String mockContent = "[STUB RESPONSE] Based on the analysis of " + request.ruleId() +
        ", the code appears to follow standard practices, but consider verifying the reactive pipeline implementation.";

    return Mono.just(new LlmResponse(
        mockContent,
        model,
        150, // mock prompt tokens
        50,  // mock completion tokens
        0
    ));
  }

  @Override
  public boolean isTier2() {
    return tier2;
  }

  @Override
  public LlmProvider provider() {
    return LlmProvider.STUB;
  }
}
