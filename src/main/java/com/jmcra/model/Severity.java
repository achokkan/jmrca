package com.jmcra.model;

/**
 * Finding severity levels, ordered from most to least critical.
 * Maps directly to the {@code severity} field in FindingSchema v1.
 * <p>
 * Spec: Section 4.2 (Stage 4 — Rule Evaluators), Section 5 (Rule Catalog).
 */
public enum Severity {

  /** Use for CVEs with CVSS ≥ 9.0, hardcoded credentials. Gates CI on non-zero exit. */
  CRITICAL(5),

  /** Use for circuit-breaker absence, SQL injection, JWT secret weakness. */
  HIGH(4),

  /** Use for missing @PreAuthorize, parameterised-log violations. */
  MEDIUM(3),

  /** Use for informational style issues (e.g., API versioning hints). */
  LOW(2),

  /** For informational observations only (e.g., DES-008 compact source file). */
  INFO(1);

  /** Numeric weight used by RankDedupeService for severity-DESC ranking. */
  private final int weight;

  Severity(int weight) {
    this.weight = weight;
  }

  public int weight() {
    return weight;
  }

  /** Health-score penalty per finding at this severity (per NFR in Section 9). */
  public double penaltyPoints() {
    return switch (this) {
      case CRITICAL -> 20.0;
      case HIGH     -> 10.0;
      case MEDIUM   ->  5.0;
      case LOW      ->  2.0;
      case INFO     ->  0.5;
    };
  }
}
