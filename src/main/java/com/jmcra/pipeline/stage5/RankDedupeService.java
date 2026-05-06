package com.jmcra.pipeline.stage5;

import com.jmcra.config.JmcraProperties;
import com.jmcra.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Stage 5 — Rank &amp; Deduplicate.
 *
 * <h3>Responsibilities (per SDD Section 4.2)</h3>
 * <ol>
 *   <li>Merges findings from all domain workers.</li>
 *   <li>Deduplicates by key: {@code (ruleId + file + lineRange)}.</li>
 *   <li>Applies team-level suppression rules from {@code .jmcra/suppressions.yml}.</li>
 *   <li>Ranks by: severity DESC, confidence DESC, file churn frequency DESC.</li>
 *   <li>Computes the overall {@link HealthScore}.</li>
 *   <li>Assembles the final {@link FindingsReport}.</li>
 * </ol>
 *
 * Spec: Section 4.2 (Stage 5 — Rank &amp; Deduplicate).
 */
@Service
public class RankDedupeService {

  private static final Logger log = LoggerFactory.getLogger(RankDedupeService.class);

  private final JmcraProperties props;

  public RankDedupeService(JmcraProperties props) {
    this.props = props;
  }

  // ── Public API ──────────────────────────────────────────────────────────────

  /**
   * Deduplicates, applies suppressions, ranks, and assembles the final report.
   *
   * @param allFindings Full unordered list of findings from Stage 3/4.
   * @param ctx         Domain context (provides scan request, parse errors, repo root).
   * @return A {@code Mono} emitting the assembled {@link FindingsReport}.
   */
  public Mono<FindingsReport> rankAndDedupe(
      List<Finding> allFindings,
      com.jmcra.pipeline.stage4.DomainContext ctx) {

    return Mono.fromCallable(() -> {
      var deduplicated = deduplicate(allFindings);
      var suppressions = loadSuppressions(ctx.repositoryRoot());
      var suppressed   = applySuppressions(deduplicated, suppressions);
      var ranked       = rank(suppressed);
      var report       = FindingsReport.builder(ctx.scanRequest())
          .findings(ranked)
          .parseErrors(ctx.astIndex().parseErrors())
          .build();

      log.info("[{}] Rank/dedupe complete: {} raw → {} dedup → {} suppressed → {} final (score={})",
          ctx.scanRequest().scanId(),
          allFindings.size(), deduplicated.size(), suppressed.size(), ranked.size(),
          String.format("%.1f", report.healthScore().score()));
      return report;
    });
  }

  // ── Deduplication ──────────────────────────────────────────────────────────

  /**
   * Deduplication key: {@code ruleId + "|" + file + "|" + line}.
   * When duplicates exist, keeps the finding with the highest confidence score.
   */
  private List<Finding> deduplicate(List<Finding> findings) {
    return findings.stream()
        .collect(Collectors.toMap(
            f -> f.ruleId() + "|" + f.file() + "|" + f.line(),
            f -> f,
            (a, b) -> a.confidence() >= b.confidence() ? a : b,
            LinkedHashMap::new
        ))
        .values()
        .stream()
        .toList();
  }

  // ── Suppressions ───────────────────────────────────────────────────────────

  private record Suppression(String ruleId, String file, String reason) {}

  private List<Suppression> loadSuppressions(Path repoRoot) {
    Path suppressions = repoRoot.resolve(".jmcra/suppressions.yml");
    if (!Files.exists(suppressions)) return List.of();

    try {
      String content = Files.readString(suppressions);
      return parseSuppressions(content);
    } catch (IOException e) {
      log.warn("Failed to read suppressions.yml: {}", e.getMessage());
      return List.of();
    }
  }

  private List<Suppression> parseSuppressions(String yaml) {
    var list = new ArrayList<Suppression>();
    var lines = yaml.split("\n");
    String ruleId = null, file = null, reason = null;
    for (String line : lines) {
      line = line.strip();
      if (line.startsWith("ruleId:"))  ruleId = line.substring(7).strip();
      if (line.startsWith("file:"))    file   = line.substring(5).strip();
      if (line.startsWith("reason:"))  reason = line.substring(7).strip();
      if (line.startsWith("- ruleId:")) {
        if (ruleId != null) list.add(new Suppression(ruleId, file, reason));
        ruleId = line.substring(9).strip(); file = null; reason = null;
      }
    }
    if (ruleId != null) list.add(new Suppression(ruleId, file, reason));
    return list;
  }

  private List<Finding> applySuppressions(List<Finding> findings,
                                           List<Suppression> suppressions) {
    if (suppressions.isEmpty()) return findings;
    return findings.stream()
        .filter(f -> suppressions.stream().noneMatch(s -> matches(f, s)))
        .toList();
  }

  private boolean matches(Finding f, Suppression s) {
    boolean ruleMatch = f.ruleId().equals(s.ruleId());
    boolean fileMatch = s.file() == null || f.file().contains(s.file());
    return ruleMatch && fileMatch;
  }

  // ── Ranking ────────────────────────────────────────────────────────────────

  /**
   * Ranks findings by: severity DESC → confidence DESC → ruleId ASC (stable tie-break).
   * "File churn frequency DESC" is a Phase 4 enhancement (requires SCM commit history).
   */
  private List<Finding> rank(List<Finding> findings) {
    return findings.stream()
        .sorted(Comparator
            .comparingInt((Finding f) -> f.severity().weight()).reversed()
            .thenComparingDouble(Finding::confidence).reversed()
            .thenComparing(Finding::ruleId))
        .toList();
  }
}
