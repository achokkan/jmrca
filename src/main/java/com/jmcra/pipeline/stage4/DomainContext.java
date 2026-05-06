package com.jmcra.pipeline.stage4;

import com.jmcra.model.DependencyTree;
import com.jmcra.model.ScanRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The context object passed to every {@link RuleEvaluator} during Stage 4.
 * Contains all parsed artefacts produced by Stage 2 (Parse &amp; Index).
 *
 * <h3>Thread Safety</h3>
 * {@code DomainContext} instances are effectively immutable after construction.
 * All collections are unmodifiable views. Safe to share across parallel domain workers.
 * <p>
 * Spec: Section 4.2 (Stage 3 — Analysis Dispatch):
 * "Each domain worker receives a DomainContext: AST index, call graph, config files, dependency tree."
 *
 * @param scanRequest        The original normalised scan request.
 * @param astIndex           JavaParser AST + JGraphT call-graph index (Stage 2 output).
 * @param dependencyTree     Parsed Maven/Gradle dependency graph.
 * @param configProperties   Flattened key=value view of all YAML/properties config files.
 * @param configFilePaths    Absolute paths to all discovered config files.
 * @param repositoryRoot     Absolute path to the cloned repository root.
 * @param fileLineCounts     Map of relative path → line count for line-number validation.
 */
public record DomainContext(
    ScanRequest          scanRequest,
    AstIndex             astIndex,
    DependencyTree       dependencyTree,
    Map<String, String>  configProperties,
    List<Path>           configFilePaths,
    Path                 repositoryRoot,
    Map<String, Integer> fileLineCounts
) {

  public DomainContext {
    Objects.requireNonNull(scanRequest,      "scanRequest must not be null");
    Objects.requireNonNull(astIndex,         "astIndex must not be null");
    Objects.requireNonNull(dependencyTree,   "dependencyTree must not be null");
    Objects.requireNonNull(repositoryRoot,   "repositoryRoot must not be null");
    configProperties = configProperties != null
        ? Map.copyOf(configProperties) : Map.of();
    configFilePaths  = configFilePaths  != null
        ? List.copyOf(configFilePaths)  : List.of();
    fileLineCounts   = fileLineCounts   != null
        ? Map.copyOf(fileLineCounts)    : Map.of();
  }

  // ── Convenience Accessors ──────────────────────────────────────────────────

  /**
   * Returns the number of lines in the given source file.
   * Used by {@code FindingSchemaOracleTest} (SPC-031) to validate that
   * reported line numbers are within the actual file bounds.
   *
   * @param relativeFilePath Relative path from repository root.
   * @return Line count, or {@code Integer.MAX_VALUE} if the file is unknown.
   */
  public int fileLineCount(String relativeFilePath) {
    return fileLineCounts.getOrDefault(relativeFilePath, Integer.MAX_VALUE);
  }

  /**
   * Returns the value of a configuration property, or {@code null} if absent.
   * Supports dot-separated keys normalised from YAML (e.g., "spring.datasource.url").
   */
  public String configProperty(String key) {
    return configProperties.get(key);
  }

  /** Returns {@code true} if the given configuration key is present. */
  public boolean hasConfigProperty(String key) {
    return configProperties.containsKey(key);
  }

  /**
   * Creates a synthetic context for unit tests. Produces a minimal context
   * with empty AST, dependency tree, and config — sufficient for rule contract tests.
   */
  public static DomainContext synthetic() {
    var scanRequest = ScanRequest.builder()
        .repositoryUrl("https://github.com/test/repo")
        .commitSha("a".repeat(40))
        .branch("main")
        .build();
    return new DomainContext(
        scanRequest,
        AstIndex.empty(),
        DependencyTree.empty("maven"),
        Map.of(),
        List.of(),
        Path.of(System.getProperty("java.io.tmpdir")),
        Map.of()
    );
  }
}
