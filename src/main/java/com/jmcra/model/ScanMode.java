package com.jmcra.model;

/**
 * Determines the scope of files analysed during a scan.
 * <p>
 * Spec: Section 4.2 (Stage 1 — Ingest), Stage 2 (Parse &amp; Index).
 */
public enum ScanMode {

  /**
   * Analyse all Java source files in the repository.
   * Used for scheduled scans.
   */
  FULL,

  /**
   * Analyse only changed files (delta) from the PR diff.
   * Used for PR webhook triggers. Reduces latency significantly.
   */
  DIFF_ONLY
}
