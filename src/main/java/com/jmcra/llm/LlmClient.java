package com.jmcra.llm;

import reactor.core.publisher.Mono;

/**
 * Abstraction for all LLM provider implementations.
 * Implementations are selected by {@link LlmClientFactory} based on
 * {@code jmcra.llm.provider} configuration.
 *
 * <h3>Contract</h3>
 * <ul>
 *   <li>Returns a cold {@code Mono} — each subscription triggers a new HTTP call.</li>
 *   <li>Implements retry up to 2 times on malformed JSON responses (Section 6.3).</li>
 *   <li>Applies token-bucket rate limiting to prevent CI pipeline slowdown (Section 10).</li>
 *   <li>Never throws; errors are propagated as {@code onError} signals.</li>
 * </ul>
 *
 * Spec: Section 6.1 (Model Selection Rationale), Section 6.3 (Hallucination Mitigation).
 */
public interface LlmClient {

  /**
   * Sends the given request to the LLM provider and returns the raw completion.
   *
   * @param request The fully-constructed prompt request.
   * @return A {@code Mono} that emits the LLM response or errors on permanent failure.
   */
  Mono<LlmResponse> chat(LlmRequest request);

  /**
   * Returns {@code true} if this client handles Tier-2 (deep analysis) requests.
   * Tier-2 clients use more capable (and more expensive) models.
   */
  boolean isTier2();

  /**
   * The provider enum this client implements.
   */
  LlmProvider provider();
}
