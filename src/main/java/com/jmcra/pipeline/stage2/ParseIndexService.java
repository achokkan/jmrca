package com.jmcra.pipeline.stage2;

import com.jmcra.ast.AstIndexer;
import com.jmcra.config.JmcraProperties;
import com.jmcra.model.*;
import com.jmcra.pipeline.stage4.AstIndex;
import com.jmcra.pipeline.stage4.DomainContext;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * Stage 2 — Parse &amp; Index.
 *
 * <h3>Responsibilities</h3>
 * <ol>
 *   <li>Clones the repository (shallow depth=1) via JGit.</li>
 *   <li>In DIFF_ONLY mode: fetches the changed-files list from the SCM API.</li>
 *   <li>Builds the {@link AstIndex} via {@link AstIndexer} (JavaParser 3.28).</li>
 *   <li>Parses the build file (pom.xml / build.gradle) into a {@link DependencyTree}.</li>
 *   <li>Reads all config files (application.yml, *.properties) into a flat property map.</li>
 *   <li>Assembles and returns the {@link DomainContext} for Stage 3.</li>
 * </ol>
 *
 * <h3>Error Handling</h3>
 * Parse errors are non-fatal; files with parse errors are collected in
 * {@link AstIndex#parseErrors()} and reported in the final {@link FindingsReport}.
 * <p>
 * Spec: Section 4.2 (Stage 2 — Parse &amp; Index). ADR-001 (JavaParser). ADR-002 (Reactor).
 */
@Service
public class ParseIndexService {

  private static final Logger log = LoggerFactory.getLogger(ParseIndexService.class);

  private final AstIndexer      astIndexer;
  private final JmcraProperties props;

  public ParseIndexService(AstIndexer astIndexer, JmcraProperties props) {
    this.astIndexer = astIndexer;
    this.props      = props;
  }

  // ── Public API ──────────────────────────────────────────────────────────────

  /**
   * Clones the repository, builds the AST index, and returns a fully populated
   * {@link DomainContext} ready for parallel domain dispatch.
   *
   * @param scanRequest The normalised scan request from Stage 1.
   * @return A {@code Mono} emitting the fully populated {@link DomainContext}.
   */
  public Mono<DomainContext> parseAndIndex(ScanRequest scanRequest) {
    return Mono.fromCallable(() -> execute(scanRequest))
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess(ctx -> log.info("[{}] Parse complete: {} files indexed, {} deps",
            scanRequest.scanId(),
            ctx.astIndex().compilationUnits().size(),
            ctx.dependencyTree().allDependencies().size()))
        .doOnError(e -> log.error("[{}] Parse stage failed: {}", scanRequest.scanId(), e.getMessage()));
  }

  // ── Internal Execution ─────────────────────────────────────────────────────

  private DomainContext execute(ScanRequest scanRequest) throws IOException, GitAPIException {
    Path workspaceRoot = resolveWorkspace(scanRequest);
    cloneRepository(scanRequest, workspaceRoot);

    List<String> targetFiles = scanRequest.isDiffOnly()
        ? scanRequest.changedFiles()
        : List.of(); // empty = all files

    DependencyResult depResult = parseDependencies(workspaceRoot);
    AstIndex        astIndex        = astIndexer.buildIndex(workspaceRoot, targetFiles)
                                         .withMetadata(depResult.javaVersion, depResult.frameworks);
    
    List<Path>      configFiles     = findConfigFiles(workspaceRoot);
    Map<String,String> configProps  = flattenConfig(configFiles);
    Map<String,Integer> lineCounts  = buildLineCountMap(workspaceRoot, astIndex);

    return new DomainContext(
        scanRequest,
        astIndex,
        depResult.tree,
        configProps,
        configFiles,
        workspaceRoot,
        lineCounts
    );
  }

  private record DependencyResult(DependencyTree tree, String javaVersion, Map<String, String> frameworks) {}

  // ── Repository Clone ───────────────────────────────────────────────────────

  private Path resolveWorkspace(ScanRequest req) throws IOException {
    Path base = Path.of(props.git().cloneDir());
    Path dir  = base.resolve(req.scanId());
    Files.createDirectories(dir);
    return dir;
  }

  private void cloneRepository(ScanRequest req, Path targetDir) throws GitAPIException, IOException {
    if (Files.exists(targetDir.resolve(".git"))) {
      log.debug("[{}] Workspace already exists, skipping clone/copy", req.scanId());
      return;
    }

    if (req.repositoryUrl().startsWith("local://")) {
      String localPath = req.repositoryUrl().substring(8);
      log.info("[{}] Local scan detected — copying from {}", req.scanId(), localPath);
      copyLocalRepository(Path.of(localPath), targetDir);
      return;
    }

    log.info("[{}] Cloning {} @ {} (depth={})",
        req.scanId(), req.repositoryUrl(), req.branch(), props.git().cloneDepth());

    Git.cloneRepository()
        .setURI(req.repositoryUrl())
        .setDirectory(targetDir.toFile())
        .setBranch(req.branch())
        .setDepth(props.git().cloneDepth())
        .call()
        .close();
  }

  private void copyLocalRepository(Path src, Path dest) throws IOException {
    try (Stream<Path> stream = Files.walk(src)) {
      stream.forEach(source -> {
        try {
          // Exclude build artifacts and Git metadata
          String pathStr = source.toString();
          if (pathStr.contains(".git") || pathStr.contains("/target/") || pathStr.contains("\\target\\")) {
            return;
          }
          Path destination = dest.resolve(src.relativize(source));
          if (Files.isDirectory(source)) {
            Files.createDirectories(destination);
          } else {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
          }
        } catch (IOException e) {
          log.warn("Failed to copy file {}: {}", source, e.getMessage());
        }
      });
    }
  }

  // ── Dependency Parsing ─────────────────────────────────────────────────────

  private DependencyResult parseDependencies(Path repoRoot) {
    List<Dependency> allDeps = new ArrayList<>();
    Map<String, String> frameworks = new HashMap<>();
    String javaVersion = "17";

    try (Stream<Path> walk = Files.walk(repoRoot)) {
      List<Path> buildFiles = walk.filter(p -> {
        String name = p.getFileName().toString();
        return name.equals("pom.xml") || name.equals("build.gradle") || name.equals("build.gradle.kts");
      }).toList();

      for (Path buildFile : buildFiles) {
        log.info("Analyzing build file: {}", buildFile);
        if (buildFile.getFileName().toString().equals("pom.xml")) {
          String content = Files.readString(buildFile);
          allDeps.addAll(extractMavenDeps(content));
          
          // Detect Spring Boot Version (Parent or Dependencies)
          if (content.contains("spring-boot")) {
              var m = java.util.regex.Pattern.compile("<spring\\.boot\\.version>([^<]+)</spring\\.boot\\.version>").matcher(content);
              if (m.find()) {
                  frameworks.put("SPRING_BOOT", m.group(1));
                  log.info("Detected SPRING_BOOT version: {}", m.group(1));
              }
              
              var m2 = java.util.regex.Pattern.compile("<artifactId>spring-boot-starter-parent</artifactId>\\s*<version>([^<]+)</version>", java.util.regex.Pattern.DOTALL).matcher(content);
              if (m2.find()) {
                  frameworks.put("SPRING_BOOT", m2.group(1));
                  log.info("Detected SPRING_BOOT version (parent): {}", m2.group(1));
              }
          }
          
          // Detect Java Version (various properties)
          var m3 = java.util.regex.Pattern.compile("<(?:java\\.version|maven\\.compiler\\.release|maven\\.compiler\\.source|maven\\.compiler\\.target)>\\s*([^<\\s]+)\\s*</").matcher(content);
          if (m3.find()) {
              javaVersion = m3.group(1).strip();
              log.info("Detected JAVA version: {}", javaVersion);
          }
        }
      }
    } catch (IOException e) {
      log.warn("Failed to walk workspace for build files: {}", e.getMessage());
    }

    return new DependencyResult(new DependencyTree("multi-module", allDeps, allDeps), javaVersion, frameworks);
  }

  private DependencyTree parseMavenDependencies(Path pomPath) {
    try {
      String content = Files.readString(pomPath);
      List<Dependency> deps = extractMavenDeps(content);
      log.debug("Maven POM parsed: {} direct dependencies", deps.size());
      return new DependencyTree("maven", deps, deps); // v1: direct = all
    } catch (IOException e) {
      log.warn("Failed to parse pom.xml: {}", e.getMessage());
      return DependencyTree.empty("maven");
    }
  }

  private List<Dependency> extractMavenDeps(String pomContent) {
    var deps   = new ArrayList<Dependency>();
    var pattern = java.util.regex.Pattern.compile(
        "<dependency>\\s*<groupId>([^<]+)</groupId>\\s*<artifactId>([^<]+)</artifactId>" +
        "(?:\\s*<version>([^<]+)</version>)?(?:\\s*<scope>([^<]+)</scope>)?",
        java.util.regex.Pattern.DOTALL);
    var matcher = pattern.matcher(pomContent);
    while (matcher.find()) {
      deps.add(new Dependency(
          matcher.group(1).strip(),
          matcher.group(2).strip(),
          matcher.group(3) != null ? matcher.group(3).strip() : "managed",
          matcher.group(4) != null ? matcher.group(4).strip() : "compile",
          true
      ));
    }
    return deps;
  }

  // ── Config File Parsing ────────────────────────────────────────────────────

  private List<Path> findConfigFiles(Path repoRoot) throws IOException {
    Path resourcesDir = repoRoot.resolve("src/main/resources");
    if (!Files.exists(resourcesDir)) return List.of();

    try (Stream<Path> walk = Files.walk(resourcesDir)) {
      return walk
          .filter(Files::isRegularFile)
          .filter(p -> {
            String name = p.getFileName().toString();
            return name.endsWith(".yml") || name.endsWith(".yaml")
                || name.endsWith(".properties");
          })
          .toList();
    }
  }

  private Map<String, String> flattenConfig(List<Path> configFiles) {
    var props = new LinkedHashMap<String, String>();
    for (Path f : configFiles) {
      try {
        if (f.toString().endsWith(".properties")) {
          var p = new java.util.Properties();
          p.load(Files.newInputStream(f));
          p.forEach((k, v) -> props.put(k.toString(), v.toString()));
        } else {
          // Flatten YAML key.subkey.leaf = value using simple line parsing
          flattenYaml(Files.readString(f), props);
        }
      } catch (IOException e) {
        log.warn("Failed to read config file {}: {}", f, e.getMessage());
      }
    }
    return props;
  }

  /**
   * Minimal YAML flattening — supports simple key: value and nested keys.
   * Full YAML parsing is deferred to a SnakeYAML integration in Phase 4.
   */
  private void flattenYaml(String content, Map<String, String> out) {
    var stack = new ArrayDeque<String>();
    for (String line : content.split("\n")) {
      if (line.isBlank() || line.stripLeading().startsWith("#")) continue;
      int indent = line.length() - line.stripLeading().length();
      // Adjust stack depth based on indent (2-space YAML)
      while (stack.size() > indent / 2) stack.pop();

      String stripped = line.strip();
      int colon = stripped.indexOf(':');
      if (colon < 0) continue;
      String key   = stripped.substring(0, colon).strip();
      String value = stripped.substring(colon + 1).strip();

      String prefix = stack.isEmpty() ? "" : String.join(".", stack) + ".";
      if (!value.isEmpty()) {
        out.put(prefix + key, value.replaceAll("^['\"]|['\"]$", ""));
      } else {
        stack.push(key);
      }
    }
  }

  // ── Line Count Map ─────────────────────────────────────────────────────────

  private Map<String, Integer> buildLineCountMap(Path repoRoot, AstIndex idx) {
    var map = new HashMap<String, Integer>();
    for (String relPath : idx.compilationUnits().keySet()) {
      Path abs = repoRoot.resolve(relPath);
      try {
        int lines = (int) Files.lines(abs).count();
        map.put(relPath, lines);
      } catch (IOException e) {
        map.put(relPath, Integer.MAX_VALUE);
      }
    }
    return map;
  }
}
