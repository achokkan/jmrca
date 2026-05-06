package com.jmcra.model;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * The aggregated, ranked, deduplicated output of a complete JMCRA scan.
 * Produced by Stage 5 (Rank &amp; Deduplicate) and delivered by Stage 6.
 * <p>
 * This is the top-level payload sent to all delivery targets:
 * GitHub check runs, JIRA, Slack, and the CI exit-code gate.
 * <p>
 * Spec: Section 4.2 (Stage 5, Stage 6).
 *
 * @param scanId          Unique identifier for this scan (from {@link ScanRequest}).
 * @param repositoryUrl   The repository that was scanned.
 * @param commitSha       The commit SHA analysed.
 * @param branch          Branch name.
 * @param scanMode        FULL or DIFF_ONLY.
 * @param findings        Ranked, deduplicated findings (most severe first).
 * @param parseErrors     Non-fatal parse failures encountered in Stage 2.
 * @param healthScore     Computed health score for this scan.
 * @param scanStartedAt   Timestamp the scan began.
 * @param scanCompletedAt Timestamp the scan completed.
 * @param gateViolated    {@code true} if CRITICAL findings are present and CI gate policy fired.
 */
public record FindingsReport(
    String          scanId,
    String          repositoryUrl,
    String          commitSha,
    String          branch,
    ScanMode        scanMode,
    List<Finding>   findings,
    List<ParseError> parseErrors,
    HealthScore     healthScore,
    Instant         scanStartedAt,
    Instant         scanCompletedAt,
    boolean         gateViolated
) {

  public FindingsReport {
    Objects.requireNonNull(scanId,          "scanId must not be null");
    Objects.requireNonNull(repositoryUrl,   "repositoryUrl must not be null");
    Objects.requireNonNull(healthScore,     "healthScore must not be null");
    Objects.requireNonNull(scanStartedAt,   "scanStartedAt must not be null");
    Objects.requireNonNull(scanCompletedAt, "scanCompletedAt must not be null");
    findings    = findings    != null ? List.copyOf(findings)    : List.of();
    parseErrors = parseErrors != null ? List.copyOf(parseErrors) : List.of();
  }

  /** Total elapsed scan time. */
  public Duration scanDuration() {
    return Duration.between(scanStartedAt, scanCompletedAt);
  }

  /** Top-N findings by severity and confidence (for Slack summary cards). */
  public List<Finding> topFindings(int n) {
    return findings.stream().limit(n).toList();
  }

  /** Returns findings that should trigger JIRA sub-task creation (CRITICAL + HIGH). */
  public List<Finding> priorityFindings() {
    return findings.stream()
        .filter(f -> f.severity() == Severity.CRITICAL || f.severity() == Severity.HIGH)
        .toList();
  }

  /** Convenience builder. */
  public static Builder builder(ScanRequest request) {
    return new Builder(request);
  }

  public static final class Builder {
    private final String  scanId;
    private final String  repositoryUrl;
    private final String  commitSha;
    private final String  branch;
    private final ScanMode scanMode;
    private final Instant  scanStartedAt = Instant.now();
    private List<Finding>   findings    = List.of();
    private List<ParseError> parseErrors = List.of();
    private HealthScore     healthScore = HealthScore.perfect();
    private boolean         gateViolated = false;

    private Builder(ScanRequest req) {
      this.scanId        = req.scanId();
      this.repositoryUrl = req.repositoryUrl();
      this.commitSha     = req.commitSha();
      this.branch        = req.branch();
      this.scanMode      = req.scanMode();
    }

    public Builder findings(List<Finding> findings) {
      this.findings    = findings;
      this.healthScore = HealthScore.compute(findings);
      this.gateViolated = healthScore.hasCritical();
      return this;
    }

    public Builder parseErrors(List<ParseError> errors) {
      this.parseErrors = errors;
      return this;
    }

    public FindingsReport build() {
      return new FindingsReport(scanId, repositoryUrl, commitSha, branch, scanMode,
          findings, parseErrors, healthScore, scanStartedAt, Instant.now(), gateViolated);
    }
  }
}
