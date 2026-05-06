package com.jmcra.ast;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.jmcra.model.ParseError;
import com.jmcra.pipeline.stage4.AstIndex;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Builds the {@link AstIndex} from a cloned repository using JavaParser 3.28.
 *
 * <h3>What it does</h3>
 * <ol>
 *   <li>Configures symbol resolution via {@link JavaSymbolSolver} with classpath and source-root solvers.</li>
 *   <li>Walks all {@code .java} files (or only changed files in DIFF_ONLY mode).</li>
 *   <li>Parses each file into a JavaParser {@link CompilationUnit}.</li>
 *   <li>Extracts the annotation index (annotation name → list of annotated type FQNs).</li>
 *   <li>Extracts the import graph (FQCN → imports).</li>
 *   <li>Builds a directed call graph using JGraphT (method-level edges).</li>
 *   <li>Collects non-fatal parse errors without blocking other files.</li>
 * </ol>
 *
 * <h3>Java 25 Support</h3>
 * JavaParser 3.28 supports Java 1–25, including scoped values (JEP 506),
 * primitive patterns (JEP 507), and compact source files (JEP 512).
 * <p>
 * Spec: Section 4.2 (Stage 2 — Parse &amp; Index), ADR-001 (JavaParser over JDT).
 */
@Component
public class AstIndexer {

  private static final Logger log = LoggerFactory.getLogger(AstIndexer.class);

  /**
   * Builds a complete {@link AstIndex} for the given repository root and file list.
   *
   * @param repositoryRoot Absolute path to the cloned repository (Stage 2 workspace).
   * @param targetFiles    Relative paths to analyse (empty = all .java files under src/main).
   * @return The populated AST index, including any non-fatal parse errors.
   */
  public AstIndex buildIndex(Path repositoryRoot, List<String> targetFiles) {
    log.info("Building AST index for [{}], targetFiles={}", repositoryRoot,
        targetFiles.isEmpty() ? "ALL" : targetFiles.size() + " files");

    // Detect versions first so we can configure the parser correctly
    String detectedJava = detectJavaVersion(repositoryRoot);
    Map<String, String> detectedFrameworks = detectFrameworks(repositoryRoot);

    JavaParser parser = configureParser(repositoryRoot, detectedJava);

    Map<String, CompilationUnit> cuMap      = new HashMap<>();
    Map<String, List<String>>    importGraph = new HashMap<>();
    Map<String, List<String>>    annoIndex   = new HashMap<>();
    Graph<String, DefaultEdge>   callGraph   = new DefaultDirectedGraph<>(DefaultEdge.class);
    List<ParseError>             parseErrors = new ArrayList<>();
    AtomicInteger                parsed      = new AtomicInteger(0);

    List<Path> filesToParse = resolveFiles(repositoryRoot, targetFiles);

    for (Path file : filesToParse) {
      String relativePath = repositoryRoot.relativize(file).toString().replace("\\", "/");
      try {
        CompilationUnit cu = parser.parse(file).getResult()
            .orElseThrow(() -> new ParseProblemException(new ArrayList<>()));
        cuMap.put(relativePath, cu);
        indexAnnotations(cu, relativePath, annoIndex);
        indexImports(cu, relativePath, importGraph);
        indexCallGraph(cu, callGraph);
        parsed.incrementAndGet();
      } catch (ParseProblemException e) {
        log.warn("Parse error in [{}]: {}", relativePath, e.getMessage());
        parseErrors.add(ParseError.of(relativePath, e.getProblems().get(0).getMessage()));
      } catch (IOException e) {
        log.warn("IO error reading [{}]: {}", relativePath, e.getMessage());
        parseErrors.add(ParseError.of(relativePath, "IO error: " + e.getMessage()));
      }
    }

    log.info("AST index built: {} files parsed, {} errors", parsed.get(), parseErrors.size());

    return new AstIndex(cuMap, callGraph, importGraph, annoIndex, parseErrors,
        detectedJava, detectedFrameworks);
  }

  // ── Symbol Resolution Setup ────────────────────────────────────────────────

  private JavaParser configureParser(Path repoRoot, String javaVersion) {
    var typeSolver = new CombinedTypeSolver();
    typeSolver.add(new ReflectionTypeSolver(false));

    // Add JavaParser source type solver for the main source directory
    Path mainSrc = repoRoot.resolve("src/main/java");
    if (Files.exists(mainSrc)) {
      typeSolver.add(new JavaParserTypeSolver(mainSrc));
    }
    Path testSrc = repoRoot.resolve("src/test/java");
    if (Files.exists(testSrc)) {
      typeSolver.add(new JavaParserTypeSolver(testSrc));
    }

    ParserConfiguration.LanguageLevel level = ParserConfiguration.LanguageLevel.JAVA_21;
    try {
        int version = Integer.parseInt(javaVersion.split("\\.")[0]);
        if (version >= 25) level = ParserConfiguration.LanguageLevel.JAVA_25;
        else if (version >= 21) level = ParserConfiguration.LanguageLevel.JAVA_21;
        else if (version >= 17) level = ParserConfiguration.LanguageLevel.JAVA_17;
    } catch (Exception e) {
        log.warn("Invalid java version [{}], defaulting to JAVA_21", javaVersion);
    }

    var parserConfig = new ParserConfiguration()
        .setSymbolResolver(new JavaSymbolSolver(typeSolver))
        .setLanguageLevel(level);

    return new JavaParser(parserConfig);
  }

  // ── File Resolution ────────────────────────────────────────────────────────

  private List<Path> resolveFiles(Path repoRoot, List<String> targetFiles) {
    if (!targetFiles.isEmpty()) {
      // DIFF_ONLY mode: analyse only changed files that are Java source
      return targetFiles.stream()
          .filter(f -> f.endsWith(".java"))
          .map(repoRoot::resolve)
          .filter(Files::exists)
          .toList();
    }

    // FULL mode: walk all .java files under src/main/java
    Path srcMain = repoRoot.resolve("src/main/java");
    if (!Files.exists(srcMain)) {
      log.warn("src/main/java not found at [{}], scanning repo root", repoRoot);
      srcMain = repoRoot;
    }

    try (Stream<Path> walk = Files.walk(srcMain)) {
      List<Path> files = walk.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".java"))
                 .filter(Files::isRegularFile)
                 .toList();
      log.info("Resolved {} Java files for indexing under [{}]", files.size(), srcMain);
      return files;
    } catch (IOException e) {
      log.error("Failed to walk source directory [{}]", srcMain, e);
      return List.of();
    }
  }

  // ── Annotation Index ───────────────────────────────────────────────────────

  private void indexAnnotations(CompilationUnit cu, String filePath,
                                Map<String, List<String>> annoIndex) {
    cu.findAll(AnnotationExpr.class).forEach(anno -> {
      String name = anno.getNameAsString();
      annoIndex.computeIfAbsent(name, _ -> new ArrayList<>()).add(filePath);
    });
  }

  // ── Import Graph ───────────────────────────────────────────────────────────

  private void indexImports(CompilationUnit cu, String filePath,
                             Map<String, List<String>> importGraph) {
    List<String> imports = cu.getImports().stream()
        .map(i -> i.getNameAsString())
        .toList();
    if (!imports.isEmpty()) {
      importGraph.put(filePath, imports);
    }
  }

  // ── Call Graph (JGraphT) ───────────────────────────────────────────────────

  private void indexCallGraph(CompilationUnit cu, Graph<String, DefaultEdge> callGraph) {
    String packageName = cu.getPackageDeclaration()
        .map(p -> p.getNameAsString() + ".")
        .orElse("");

    cu.findAll(MethodDeclaration.class).forEach(method -> {
      String callerVertex = packageName + method.getNameAsString();
      callGraph.addVertex(callerVertex);

      method.findAll(MethodCallExpr.class).forEach(call -> {
        String calleeVertex = call.getNameAsString();
        callGraph.addVertex(calleeVertex);
        try {
          callGraph.addEdge(callerVertex, calleeVertex);
        } catch (IllegalArgumentException ignored) {
          // Self-loops or duplicate edges — benign
        }
      });
    });
  }

  // ── Version Detection ──────────────────────────────────────────────────────

  private String detectJavaVersion(Path repoRoot) {
    // Try pom.xml first with robust regex
    Path pom = repoRoot.resolve("pom.xml");
    if (Files.exists(pom)) {
      try {
        String content = Files.readString(pom);
        var m = java.util.regex.Pattern.compile("<(?:java\\.version|maven\\.compiler\\.release|maven\\.compiler\\.source|maven\\.compiler\\.target)>\\s*([^<\\s]+)\\s*</").matcher(content);
        if (m.find()) return m.group(1).strip();
      } catch (IOException ignored) {}
    }
    return "17"; 
  }

  private Map<String, String> detectFrameworks(Path repoRoot) {
    Map<String, String> frameworks = new HashMap<>();
    Path pom = repoRoot.resolve("pom.xml");
    if (Files.exists(pom)) {
      try {
        String content = Files.readString(pom);
        extractVersion(content, "spring-boot-starter-parent",
            "<version>", "</version>", frameworks, "SPRING_BOOT");
        extractProperty(content, "<spring-cloud\\.version>",
            "</spring-cloud\\.version>", frameworks, "SPRING_CLOUD");
        extractProperty(content, "<kafka\\.version>",
            "</kafka\\.version>", frameworks, "KAFKA");
        extractProperty(content, "<resilience4j\\.version>",
            "</resilience4j\\.version>", frameworks, "RESILIENCE4J");
      } catch (IOException ignored) {}
    }
    return frameworks;
  }

  private void extractVersion(String content, String artifact, String open, String close,
                               Map<String, String> map, String key) {
    int idx = content.indexOf(artifact);
    if (idx < 0) return;
    int start = content.indexOf(open, idx);
    int end   = content.indexOf(close, start + open.length());
    if (start < 0 || end < 0) return;
    map.put(key, content.substring(start + open.length(), end).strip());
  }

  private void extractProperty(String content, String openRegex, String closeRegex,
                                Map<String, String> map, String key) {
    var m = java.util.regex.Pattern.compile(openRegex + "([^<]+)" + closeRegex).matcher(content);
    if (m.find()) map.put(key, m.group(1).strip());
  }
}
