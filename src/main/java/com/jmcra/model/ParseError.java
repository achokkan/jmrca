package com.jmcra.model;

/**
 * Represents a non-fatal JavaParser error encountered during Stage 2 (Parse &amp; Index).
 * Files with parse errors are reported in the {@link FindingsReport} but do not
 * block analysis of other files.
 * <p>
 * Spec: Section 4.2 (Stage 2 — Parse &amp; Index).
 *
 * @param filePath     Relative path of the file that failed to parse.
 * @param errorMessage Human-readable description of the parse failure.
 * @param line         Source line where the parse error occurred, or -1 if unknown.
 */
public record ParseError(
    String filePath,
    String errorMessage,
    int line
) {

  public static ParseError of(String filePath, String errorMessage) {
    return new ParseError(filePath, errorMessage, -1);
  }

  public static ParseError of(String filePath, String errorMessage, int line) {
    return new ParseError(filePath, errorMessage, line);
  }
}
