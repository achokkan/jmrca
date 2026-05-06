package com.jmcra.config;

import com.jmcra.llm.LlmProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;
import java.util.List;

/**
 * Strongly-typed configuration properties for JMCRA.
 * All properties are under the {@code jmcra.*} prefix in {@code application.yml}.
 * <p>
 * Spec: Section 4.2 (Stage 1–6 configuration), Section 6.1 (LLM tier config),
 *       Section 9 (NFR — latency, concurrency).
 */
@ConfigurationProperties(prefix = "jmcra")
public record JmcraProperties(
    Ingest   ingest,
    Pipeline pipeline,
    Llm      llm,
    Delivery delivery,
    Git       git
) {

  // ── Ingest ─────────────────────────────────────────────────────────────────

  public record Ingest(
      /** Webhook HMAC secret (injected from environment / Vault). */
      String webhookSecret,
      /** Supported webhook source types. */
      @DefaultValue({"github", "gitlab"}) List<String> sources
  ) {}

  // ── Pipeline ───────────────────────────────────────────────────────────────

  public record Pipeline(
      /** Maximum number of domain workers dispatched in parallel (Stage 3). */
      @DefaultValue("4") int domainConcurrency,
      /** Timeout per domain worker (Stage 3). */
      @DefaultValue("120s") Duration domainTimeout,
      /** Whether to enable SonarQube baseline deduplication in Stage 5. */
      @DefaultValue("false") boolean sonarQubeEnabled,
      /** SonarQube base URL (required if sonarQubeEnabled = true). */
      String sonarQubeUrl,
      /** SonarQube API token. */
      String sonarQubeToken
  ) {}

  // ── LLM ───────────────────────────────────────────────────────────────────

  public record Llm(
      /** Active LLM provider. Must match one of: CLAUDE, OPENAI, GEMINI. */
      @DefaultValue("CLAUDE") LlmProvider provider,
      /** Claude-specific configuration. */
      Claude claude,
      /** OpenAI-specific configuration. */
      OpenAi openai,
      /** Gemini-specific configuration. */
      Gemini gemini,
      /** Token-bucket rate-limit: max LLM calls per minute (Section 10 risk mitigation). */
      @DefaultValue("60") int rateLimitPerMinute
  ) {

    public record Claude(
        /** Anthropic API key — inject via ${ANTHROPIC_API_KEY} env var. */
        @DefaultValue("") String apiKey
    ) {}

    public record OpenAi(
        /** OpenAI API key — inject via ${OPENAI_API_KEY} env var. */
        @DefaultValue("") String apiKey
    ) {}

    public record Gemini(
        /** Google AI Studio API key — inject via ${GEMINI_API_KEY} env var. */
        @DefaultValue("") String apiKey
    ) {}
  }

  // ── Delivery ──────────────────────────────────────────────────────────────

  public record Delivery(
      /** GitHub delivery configuration. */
      GitHub github,
      /** JIRA delivery configuration. */
      Jira   jira,
      /** Slack delivery configuration. */
      Slack  slack,
      /**
       * CI gate policy: exit non-zero if any findings match.
       * Supports: "CRITICAL", "HIGH", "NONE" (disable gate).
       */
      @DefaultValue("CRITICAL") String gateThreshold
  ) {

    public record GitHub(
        @DefaultValue("false") boolean enabled,
        /** GitHub App token or PAT for posting PR comments and check runs. */
        @DefaultValue("") String token,
        /** GitHub API base URL (override for GitHub Enterprise). */
        @DefaultValue("https://api.github.com") String apiUrl
    ) {}

    public record Jira(
        @DefaultValue("false") boolean enabled,
        String baseUrl,
        /** JIRA username or email. */
        String username,
        /** JIRA API token. */
        String apiToken,
        /** Default JIRA project key for created sub-tasks (e.g., "JMCRA"). */
        String projectKey
    ) {}

    public record Slack(
        @DefaultValue("false") boolean enabled,
        /** Slack Incoming Webhook URL. */
        String webhookUrl,
        /** Number of top findings shown in the Slack summary card. */
        @DefaultValue("5") int topFindingsCount
    ) {}
  }

  // ── Git ───────────────────────────────────────────────────────────────────

  public record Git(
      /** Local directory where repositories are cloned (Stage 2). */
      @DefaultValue("/tmp/jmcra-workspace") String cloneDir,
      /** Git clone depth (shallow clone). */
      @DefaultValue("1") int cloneDepth,
      /** Whether to delete the cloned workspace after a scan completes. */
      @DefaultValue("true") boolean cleanupAfterScan
  ) {}
}
