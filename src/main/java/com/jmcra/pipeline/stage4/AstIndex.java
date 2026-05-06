package com.jmcra.pipeline.stage4;

import com.github.javaparser.ast.CompilationUnit;
import com.jmcra.model.ParseError;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The parsed AST index produced by Stage 2 (Parse &amp; Index), backed by
 * JavaParser 3.28 and JGraphT.
 *
 * <h3>Contents</h3>
 * <ul>
 *   <li>{@link #compilationUnits} — full AST per source file</li>
 *   <li>{@link #callGraph} — directed method call graph</li>
 *   <li>{@link #importGraph} — per-class imports for dependency analysis</li>
 *   <li>{@link #annotationIndex} — all annotated type/method/field declarations</li>
 *   <li>{@link #parseErrors} — non-fatal files that failed to parse</li>
 * </ul>
 *
 * Spec: Section 4.2 (Stage 2 — Parse &amp; Index):
 * "Uses JavaParser 3.28+ with symbol resolution enabled.
 *  Java 25 language features are fully parsed."
 *
 * @param compilationUnits  Map of relative file path → JavaParser {@link CompilationUnit}.
 * @param callGraph         Directed call graph: vertex = "FQN#method", edge = calls.
 * @param importGraph       Map of FQCN → list of fully-qualified import strings.
 * @param annotationIndex   Map of annotation simple-name → list of annotated element FQNs.
 * @param parseErrors       List of files that could not be parsed (non-fatal).
 * @param detectedJavaVersion Java source version detected from pom.xml / gradle config.
 * @param detectedFrameworks  Map of Framework name → detected version string.
 */
public record AstIndex(
    Map<String, CompilationUnit>      compilationUnits,
    Graph<String, DefaultEdge>        callGraph,
    Map<String, List<String>>         importGraph,
    Map<String, List<String>>         annotationIndex,
    List<ParseError>                  parseErrors,
    String                            detectedJavaVersion,
    Map<String, String>               detectedFrameworks
) {

  public AstIndex {
    Objects.requireNonNull(compilationUnits, "compilationUnits must not be null");
    Objects.requireNonNull(callGraph,        "callGraph must not be null");
    compilationUnits   = Map.copyOf(compilationUnits);
    importGraph        = importGraph        != null ? Map.copyOf(importGraph)        : Map.of();
    annotationIndex    = annotationIndex    != null ? Map.copyOf(annotationIndex)    : Map.of();
    parseErrors        = parseErrors        != null ? List.copyOf(parseErrors)       : List.of();
    detectedFrameworks = detectedFrameworks != null ? Map.copyOf(detectedFrameworks) : Map.of();
    detectedJavaVersion = detectedJavaVersion != null ? detectedJavaVersion : "17";
  }

  /** Returns a copy of the index with updated version metadata. */
  public AstIndex withMetadata(String javaVersion, Map<String, String> frameworks) {
    return new AstIndex(compilationUnits, callGraph, importGraph, annotationIndex, 
                       parseErrors, javaVersion, frameworks);
  }

  // ── Convenience Helpers for Rule Evaluators ────────────────────────────────

  /** Returns all source file paths in the index. */
  public Set<String> allFiles() {
    return compilationUnits.keySet();
  }

  /**
   * Returns all class/interface names (simple name) annotated with the given annotation.
   * E.g., {@code annotatedWith("RestController")} returns all Spring controller FQNs.
   */
  public List<String> annotatedWith(String annotationSimpleName) {
    return annotationIndex.getOrDefault(annotationSimpleName, List.of());
  }

  /** Returns {@code true} if any source file imports the given fully-qualified class name. */
  public boolean anyFileImports(String fqcn) {
    return importGraph.values().stream().anyMatch(imports -> imports.contains(fqcn));
  }

  /**
   * Returns {@code true} if the detected Java version meets or exceeds the given major version.
   * Used by version-gated rules (e.g., Java 25 scoped values — CON-010).
   */
  public boolean isJavaVersionAtLeast(int major) {
    try {
      String v = detectedJavaVersion.split("\\.")[0].replaceAll("[^0-9]", "");
      return Integer.parseInt(v) >= major;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Returns the detected version for a given framework key (e.g., "SPRING_BOOT"),
   * or {@code "0.0.0"} if not detected.
   */
  public String frameworkVersion(String frameworkKey) {
    return detectedFrameworks.getOrDefault(frameworkKey, "0.0.0");
  }

  /** Creates an empty AstIndex for synthetic / test contexts. */
  public static AstIndex empty() {
    Graph<String, DefaultEdge> emptyGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
    return new AstIndex(Map.of(), emptyGraph, Map.of(), Map.of(), List.of(), "25", Map.of());
  }
}
