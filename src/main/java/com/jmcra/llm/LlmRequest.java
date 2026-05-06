package com.jmcra.llm;

import com.jmcra.model.Domain;

/**
 * LLM request payload sent from a {@link RuleEvaluator} to the {@link LlmClient}.
 * Contains the fully-constructed prompt and all metadata needed for logging and routing.
 * <p>
 * Spec: Section 6.2 (Prompt Architecture):
 * "Prompts are stored as versioned Markdown templates in prompts/ and loaded at runtime.
 *  No prompt literals in Java source."
 *
 * @param systemPrompt  The static system prompt (loaded from {@code prompts/<domain>-domain.md}).
 * @param userPrompt    The dynamic user prompt with code snippet and rule task.
 * @param ruleId        The rule ID requesting this LLM call (for logging and rate-limit tracking).
 * @param domain        The review domain (used for tier routing).
 * @param deepAnalysis  If {@code true}, route to Tier 2 (Sonnet/GPT-4o); else Tier 1.
 * @param maxTokens     Maximum tokens expected in the completion.
 */
public record LlmRequest(
    String  systemPrompt,
    String  userPrompt,
    String  ruleId,
    Domain  domain,
    boolean deepAnalysis,
    int     maxTokens
) {

  public LlmRequest {
    if (systemPrompt == null || systemPrompt.isBlank())
      throw new IllegalArgumentException("systemPrompt must not be blank");
    if (userPrompt == null || userPrompt.isBlank())
      throw new IllegalArgumentException("userPrompt must not be blank");
    if (maxTokens <= 0) maxTokens = 1024;
  }

  /** Convenience factory for a Tier-1 (fast) request. */
  public static LlmRequest tier1(String system, String user, String ruleId, Domain domain) {
    return new LlmRequest(system, user, ruleId, domain, false, 512);
  }

  /** Convenience factory for a Tier-2 (deep) request. */
  public static LlmRequest tier2(String system, String user, String ruleId, Domain domain) {
    return new LlmRequest(system, user, ruleId, domain, true, 2048);
  }
}
