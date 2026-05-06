package com.jmcra.model;

import java.util.Objects;

/**
 * The health score computed by Stage 5 (Rank &amp; Deduplicate).
 * Represents the overall code-quality signal for a single scan.
 * <p>
 * Formula: {@code score = max(0, 100 - Σ(finding.severity.penaltyPoints))}
 * <p>
 * Spec: Section 4.2 (Stage 5 — Rank &amp; Deduplicate):
 * "Computes overall Health Score: 100 − weighted penalty per finding."
 * Section 9 (NFR — false positive rate ≤ 5%, recall ≥ 95%).
 *
 * @param score          Computed health score in range [0.0, 100.0].
 * @param totalFindings  Total number of deduplicated findings.
 * @param criticalCount  Number of CRITICAL severity findings.
 * @param highCount      Number of HIGH severity findings.
 * @param mediumCount    Number of MEDIUM severity findings.
 * @param lowCount       Number of LOW severity findings.
 * @param infoCount      Number of INFO severity findings.
 */
public record HealthScore(
    double score,
    int    totalFindings,
    int    criticalCount,
    int    highCount,
    int    mediumCount,
    int    lowCount,
    int    infoCount
) {

  public HealthScore {
    if (score < 0.0 || score > 100.0)
      throw new IllegalArgumentException(
          "score must be in [0.0, 100.0] (was %.2f)".formatted(score));
    Objects.checkIndex(0, Math.max(1, totalFindings + 1)); // non-negative
  }

  /** A perfect score — no findings in the scan. */
  public static HealthScore perfect() {
    return new HealthScore(100.0, 0, 0, 0, 0, 0, 0);
  }

  /** Computes a HealthScore from the list of ranked findings. */
  public static HealthScore compute(java.util.List<Finding> findings) {
    if (findings.isEmpty()) return perfect();

    double penalty = findings.stream()
        .mapToDouble(f -> f.severity().penaltyPoints())
        .sum();
    double score = Math.max(0.0, 100.0 - penalty);

    long crit = findings.stream().filter(f -> f.severity() == Severity.CRITICAL).count();
    long high = findings.stream().filter(f -> f.severity() == Severity.HIGH).count();
    long med  = findings.stream().filter(f -> f.severity() == Severity.MEDIUM).count();
    long low  = findings.stream().filter(f -> f.severity() == Severity.LOW).count();
    long info = findings.stream().filter(f -> f.severity() == Severity.INFO).count();

    return new HealthScore(score, findings.size(),
        (int) crit, (int) high, (int) med, (int) low, (int) info);
  }

  /** Returns {@code true} if any CRITICAL findings are present (triggers CI gate fail). */
  public boolean hasCritical() {
    return criticalCount > 0;
  }

  /** Human-readable label: Excellent / Good / Warning / Critical. */
  public String label() {
    if (score >= 90) return "Excellent";
    if (score >= 70) return "Good";
    if (score >= 50) return "Warning";
    return "Critical";
  }
}
