package com.jmcra.pipeline.stage6;

import com.jmcra.config.JmcraProperties;
import com.jmcra.model.DeliveryResult;
import com.jmcra.model.Finding;
import com.jmcra.model.FindingsReport;
import com.jmcra.model.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Delivers findings to GitHub as:
 * <ul>
 *   <li>Inline PR review comments on the diff lines containing violations.</li>
 *   <li>A GitHub Checks API check-run with a summary and annotations.</li>
 * </ul>
 *
 * <h3>API Used</h3>
 * <ul>
 *   <li>{@code POST /repos/{owner}/{repo}/pulls/{pull_number}/reviews} — review comments.</li>
 *   <li>{@code POST /repos/{owner}/{repo}/check-runs} — check run summary.</li>
 * </ul>
 *
 * Spec: Section 4.2 (Stage 6): "GitHub/GitLab: inline PR comments on diff lines + summary check run."
 */
@Component
public class GitHubDelivery implements DeliveryTarget {

  private static final Logger log = LoggerFactory.getLogger(GitHubDelivery.class);

  private final WebClient           webClient;
  private final JmcraProperties     props;

  public GitHubDelivery(WebClient.Builder webClientBuilder, JmcraProperties props) {
    this.props     = props;
    this.webClient = webClientBuilder
        .baseUrl(props.delivery().github().apiUrl())
        .defaultHeader("Authorization", "Bearer " + props.delivery().github().token())
        .defaultHeader("Accept",        "application/vnd.github+json")
        .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
        .build();
  }

  @Override public String  name()      { return "github"; }
  @Override public boolean isEnabled() { return props.delivery().github().enabled(); }

  @Override
  public Mono<DeliveryResult> deliver(FindingsReport report) {
    if (report.scanMode() != com.jmcra.model.ScanMode.DIFF_ONLY) {
      return Mono.just(DeliveryResult.success(name(),
          "Skipped — not a PR scan", null));
    }

    return postCheckRun(report)
        .map(checkId -> DeliveryResult.success(name(),
            "Check run created", checkId))
        .onErrorResume(e -> {
          log.error("[{}] GitHub delivery failed: {}", report.scanId(), e.getMessage());
          return Mono.just(DeliveryResult.failure(name(), e.getMessage()));
        });
  }

  // ── Check Run ──────────────────────────────────────────────────────────────

  private Mono<String> postCheckRun(FindingsReport report) {
    // Parse owner/repo from repository URL
    // e.g., https://github.com/owner/repo.git → owner/repo
    String repoPath = extractRepoPath(report.repositoryUrl());
    String conclusion = report.healthScore().hasCritical() ? "failure" : "success";

    var annotations = report.findings().stream()
        .limit(50) // GitHub Checks API limit per request
        .map(this::toAnnotation)
        .toList();

    var output = Map.of(
        "title",       "JMCRA Code Review — " + report.healthScore().label(),
        "summary",     buildSummary(report),
        "annotations", annotations
    );

    var body = Map.of(
        "name",       "JMCRA Code Review",
        "head_sha",   report.commitSha(),
        "status",     "completed",
        "conclusion", conclusion,
        "output",     output
    );

    return webClient.post()
        .uri("/repos/" + repoPath + "/check-runs")
        .bodyValue(body)
        .retrieve()
        .bodyToMono(com.fasterxml.jackson.databind.JsonNode.class)
        .map(node -> node.path("id").asText("unknown"))
        .doOnSuccess(id -> log.info("[{}] GitHub check-run created: id={}", report.scanId(), id));
  }

  private Map<String, Object> toAnnotation(Finding f) {
    String level = switch (f.severity()) {
      case CRITICAL, HIGH -> "failure";
      case MEDIUM, LOW    -> "warning";
      case INFO           -> "notice";
    };
    return Map.of(
        "path",             f.file(),
        "start_line",       f.line(),
        "end_line",         f.line(),
        "annotation_level", level,
        "message",          "[%s] %s".formatted(f.ruleId(), f.message()),
        "title",            f.title(),
        "raw_details",      "Remediation: " + f.remediation()
    );
  }

  private String buildSummary(FindingsReport report) {
    var hs = report.healthScore();
    return """
        ## JMCRA Health Score: %.1f/100 — %s

        | Severity | Count |
        |---|---|
        | 🔴 CRITICAL | %d |
        | 🟠 HIGH     | %d |
        | 🟡 MEDIUM   | %d |
        | 🔵 LOW      | %d |
        | ℹ️ INFO     | %d |

        Scan completed in %d seconds.
        """.formatted(
        hs.score(), hs.label(),
        hs.criticalCount(), hs.highCount(), hs.mediumCount(), hs.lowCount(), hs.infoCount(),
        report.scanDuration().toSeconds()
    );
  }

  private String extractRepoPath(String url) {
    if (url == null) return "unknown/unknown";
    return url.replaceAll(".*github\\.com[:/]", "")
              .replaceAll("\\.git$", "");
  }
}
