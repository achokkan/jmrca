package com.jmcra.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A single violation or concern identified by a {@code RuleEvaluator}.
 * This record is the canonical representation of FindingSchema v1.
 * Every field maps 1:1 to the JSON schema defined in {@code schemas/finding-v1.json}.
 * <p>
 * Spec: Section 4.2 (Stage 4 — Rule Evaluators, Finding schema).
 *
 * @param ruleId      Stable rule identifier (e.g., "SEC-001"). Never changes once published.
 * @param severity    Severity level of the finding.
 * @param title       Short, human-readable title.
 * @param file        Relative source file path where the violation was detected.
 * @param line        1-based line number of the violation. Must be > 0.
 * @param column      1-based column offset. 0 if not applicable.
 * @param snippet     Short code snippet illustrating the violation (≤ 120 chars).
 * @param message     Detailed explanation of why this is a problem.
 * @param remediation Actionable remediation guidance.
 * @param references  List of external references (e.g., "CWE-798", "OWASP-A07:2021").
 * @param confidence  Model confidence in the finding, in range [0.0, 1.0].
 * @param ruleVersion The version of the rule that produced this finding.
 */
public record Finding(
    String ruleId,
    Severity severity,
    String title,
    String file,
    int line,
    int column,
    String snippet,
    String message,
    String remediation,
    List<String> references,
    double confidence,
    String ruleVersion
) {

  /** Compact canonical constructor — validates invariants per SDD Section 4.2. */
  public Finding {
    Objects.requireNonNull(ruleId,      "ruleId must not be null");
    Objects.requireNonNull(severity,    "severity must not be null");
    Objects.requireNonNull(title,       "title must not be null");
    Objects.requireNonNull(file,        "file must not be null");
    Objects.requireNonNull(message,     "message must not be null");
    Objects.requireNonNull(remediation, "remediation must not be null");
    Objects.requireNonNull(ruleVersion, "ruleVersion must not be null");

    if (line < 1)
      throw new IllegalArgumentException("line must be >= 1 (was %d)".formatted(line));
    if (column < 0)
      throw new IllegalArgumentException("column must be >= 0 (was %d)".formatted(column));
    if (confidence < 0.0 || confidence > 1.0)
      throw new IllegalArgumentException(
          "confidence must be in [0.0, 1.0] (was %.3f)".formatted(confidence));

    snippet    = snippet    != null ? snippet    : "";
    references = references != null ? List.copyOf(references) : List.of();
  }

  // ── Serialisation ──────────────────────────────────────────────────────────

  /**
   * Serialises this finding to a JSON string matching FindingSchema v1.
   * Used by {@code FindingSchemaOracleTest} (SPC-031) and delivery targets.
   */
  public String toJson() {
    return """
        {
          "ruleId"      : "%s",
          "severity"    : "%s",
          "title"       : "%s",
          "file"        : "%s",
          "line"        : %d,
          "column"      : %d,
          "snippet"     : %s,
          "message"     : %s,
          "remediation" : %s,
          "references"  : [%s],
          "confidence"  : %.2f,
          "ruleVersion" : "%s"
        }""".formatted(
        ruleId,
        severity.name(),
        escapeJson(title),
        escapeJson(file),
        line,
        column,
        jsonString(snippet),
        jsonString(message),
        jsonString(remediation),
        references.stream().map(r -> "\"" + r + "\"").collect(Collectors.joining(", ")),
        confidence,
        ruleVersion
    );
  }

  // ── Builder ────────────────────────────────────────────────────────────────

  public static Builder builder(String ruleId, Severity severity) {
    return new Builder(ruleId, severity);
  }

  public static final class Builder {
    private final String   ruleId;
    private final Severity severity;
    private String       title       = "";
    private String       file        = "";
    private int          line        = 1;
    private int          column      = 0;
    private String       snippet     = "";
    private String       message     = "";
    private String       remediation = "";
    private List<String> references  = List.of();
    private double       confidence  = 0.8;
    private String       ruleVersion = "1.0";

    private Builder(String ruleId, Severity severity) {
      this.ruleId   = Objects.requireNonNull(ruleId,   "ruleId");
      this.severity = Objects.requireNonNull(severity, "severity");
    }

    public Builder title(String title)              { this.title       = title;       return this; }
    public Builder file(String file)                { this.file        = file;        return this; }
    public Builder line(int line)                   { this.line        = line;        return this; }
    public Builder column(int column)               { this.column      = column;      return this; }
    public Builder snippet(String snippet)          { this.snippet     = snippet;     return this; }
    public Builder message(String message)          { this.message     = message;     return this; }
    public Builder remediation(String remediation)  { this.remediation = remediation; return this; }
    public Builder references(List<String> refs)    { this.references  = refs;        return this; }
    public Builder references(String... refs)       { this.references  = List.of(refs); return this; }
    public Builder confidence(double confidence)    { this.confidence  = confidence;  return this; }
    public Builder ruleVersion(String ruleVersion)  { this.ruleVersion = ruleVersion; return this; }

    public Finding build() {
      return new Finding(ruleId, severity, title, file, line, column,
          snippet, message, remediation, references, confidence, ruleVersion);
    }
  }

  // ── Helpers ────────────────────────────────────────────────────────────────

  private static String jsonString(String s) {
    return s == null ? "null" : "\"" + escapeJson(s) + "\"";
  }

  private static String escapeJson(String s) {
    return s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
  }
}
