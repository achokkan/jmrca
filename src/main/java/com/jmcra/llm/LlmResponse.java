package com.jmcra.llm;

/**
 * The raw completion response from an LLM provider call.
 * JSON schema validation is enforced on the {@code content} field before
 * it is parsed into {@link com.jmcra.model.Finding} instances.
 * <p>
 * Spec: Section 6.3 (Hallucination Mitigation):
 * "JSON schema validation is enforced on every LLM response;
 *  malformed output triggers a retry (max 2)."
 *
 * @param content        Raw LLM completion text (expected to be a valid JSON array of findings).
 * @param modelId        The model identifier that produced this response (e.g., "claude-haiku-3-5").
 * @param promptTokens   Number of tokens consumed by the prompt.
 * @param completionTokens Number of tokens in the completion.
 * @param retryCount     Number of retries required to get this response (0 = first attempt).
 */
public record LlmResponse(
    String content,
    String modelId,
    int    promptTokens,
    int    completionTokens,
    int    retryCount
) {

  /** Returns {@code true} if this response was produced after at least one retry. */
  public boolean wasRetried() {
    return retryCount > 0;
  }

  /** Total tokens used by this call. */
  public int totalTokens() {
    return promptTokens + completionTokens;
  }
}
