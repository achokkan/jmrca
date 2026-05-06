package com.jmcra.llm;

/**
 * Enumeration of supported LLM providers for the two-tier model strategy.
 * Selected via the {@code jmcra.llm.provider} configuration property.
 * <p>
 * Spec: Section 6.1 (Model Selection Rationale):
 * "Tier 1: Claude Haiku / GPT-4o-mini — pattern classification, severity labelling."
 * "Tier 2: Claude Sonnet / GPT-4o — complex reasoning, N+1 detection, architecture analysis."
 */
public enum LlmProvider {

  /** Anthropic Claude models (Haiku for Tier 1, Sonnet for Tier 2). */
  CLAUDE,

  /** OpenAI GPT models (GPT-4o-mini for Tier 1, GPT-4o for Tier 2). */
  OPENAI,

  /** Google Gemini models (Gemini Flash for Tier 1, Gemini Pro for Tier 2). */
  GEMINI,

  /** Local Stub provider for testing without external API calls. */
  STUB
}
