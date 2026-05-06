package com.jmcra.llm;

import com.jmcra.config.JmcraProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Selects the appropriate {@link LlmClient} implementation based on configuration
 * and tier (fast/deep) selection.
 *
 * <h3>Two-Tier Strategy (Spec Section 6.1)</h3>
 * <ul>
 *   <li><strong>Tier 1 (Fast)</strong>: Claude Haiku / GPT-4o-mini / Gemini Flash — &lt;2s per file.
 *       Used for pattern classification, severity labelling, quick-win rules.</li>
 *   <li><strong>Tier 2 (Deep)</strong>: Claude Sonnet / GPT-4o / Gemini Pro — &lt;15s per domain.
 *       Used for complex reasoning: N+1 detection, architecture anti-patterns, contract diff.</li>
 * </ul>
 *
 * <h3>Provider Selection</h3>
 * Set {@code jmcra.llm.provider} to one of: {@code CLAUDE}, {@code OPENAI}, {@code GEMINI}.
 */
@Component
public class LlmClientFactory {

  private static final Logger log = LoggerFactory.getLogger(LlmClientFactory.class);

  private final JmcraProperties         props;
  private final org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder;
  private final com.fasterxml.jackson.databind.ObjectMapper mapper;

  public LlmClientFactory(
      JmcraProperties props,
      org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder,
      com.fasterxml.jackson.databind.ObjectMapper mapper) {
    this.props            = props;
    this.webClientBuilder = webClientBuilder;
    this.mapper           = mapper;
    log.info("LLM provider configured: {} — Tier1={}, Tier2={}",
        props.llm().provider(), tier1ModelName(), tier2ModelName());
  }

  /** Returns the Tier-1 (fast / low-cost) LLM client. */
  public LlmClient tier1() {
    return create(false);
  }

  /** Returns the Tier-2 (deep / high-capability) LLM client. */
  public LlmClient tier2() {
    return create(true);
  }

  /** Creates a client for the configured provider at the specified tier. */
  public LlmClient create(boolean deepAnalysis) {
    return switch (props.llm().provider()) {
      case CLAUDE -> new ClaudeClient(webClientBuilder, mapper, props, deepAnalysis);
      case OPENAI -> new OpenAiClient(webClientBuilder, mapper, props, deepAnalysis);
      case GEMINI -> new GeminiClient(webClientBuilder, mapper, props, deepAnalysis);
      case STUB   -> new StubLlmClient(deepAnalysis);
    };
  }

  private String tier1ModelName() {
    return switch (props.llm().provider()) {
      case CLAUDE -> "claude-haiku-3-5";
      case OPENAI -> "gpt-4o-mini";
      case GEMINI -> "gemini-2.0-flash";
      case STUB   -> "stub-model";
    };
  }

  private String tier2ModelName() {
    return switch (props.llm().provider()) {
      case CLAUDE -> "claude-sonnet-4-6";
      case OPENAI -> "gpt-4o";
      case GEMINI -> "gemini-2.0-pro-exp";
      case STUB   -> "stub-model-deep";
    };
  }
}
