package com.jmcra.pipeline.stage3;

import com.jmcra.config.JmcraProperties;
import com.jmcra.model.Finding;
import com.jmcra.pipeline.stage4.DomainContext;
import com.jmcra.pipeline.stage4.RuleEvaluator;
import com.jmcra.rules.catalog.RuleCatalogLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Stage 3 — Analysis Dispatch.
 *
 * <h3>Responsibilities</h3>
 * <ol>
 *   <li>Loads the active rule catalog for the scan profile.</li>
 *   <li>Dispatches rule evaluators grouped by domain, with configurable parallelism
 *       (default: max 4 domains in parallel, per SDD spec).</li>
 *   <li>Applies version gates: rules annotated with {@code @SinceVersion} are skipped
 *       when the project's framework version does not meet the threshold.</li>
 *   <li>Each domain worker runs with a 120-second timeout (configurable).</li>
 *   <li>Domain worker failures are isolated — one domain failing does not abort others.</li>
 * </ol>
 *
 * Spec: Section 4.2 (Stage 3 — Analysis Dispatch).
 * "Dispatches domain worker Monos in parallel with configurable concurrency (default: 4)."
 * "Timeout per domain worker: 120 seconds. Worker failures are isolated."
 */
@Service
public class AnalysisDispatchService {

  private static final Logger log = LoggerFactory.getLogger(AnalysisDispatchService.class);

  private final List<RuleEvaluator>  ruleEvaluators;
  private final RuleCatalogLoader    catalogLoader;
  private final JmcraProperties      props;

  public AnalysisDispatchService(List<RuleEvaluator> ruleEvaluators,
                                 RuleCatalogLoader catalogLoader,
                                 JmcraProperties props) {
    this.ruleEvaluators = ruleEvaluators;
    this.catalogLoader  = catalogLoader;
    this.props          = props;
    log.info("AnalysisDispatchService ready: {} rule evaluators registered",
        ruleEvaluators.size());
  }

  // ── Public API ──────────────────────────────────────────────────────────────

  /**
   * Dispatches all active rule evaluators against the given domain context in parallel.
   * Returns a {@code Flux} of all findings emitted by all rules across all domains.
   *
   * @param ctx The fully-populated domain context from Stage 2.
   * @return A {@code Flux<Finding>} — complete stream of all findings.
   */
  public Flux<Finding> dispatch(DomainContext ctx) {
    int concurrency = props.pipeline().domainConcurrency();
    Duration timeout = props.pipeline().domainTimeout();

    log.info("[{}] Dispatching {} rule evaluators (concurrency={}, timeout={})",
        ctx.scanRequest().scanId(), ruleEvaluators.size(), concurrency, timeout);

    return Flux.fromIterable(activeEvaluators(ctx))
        .flatMap(evaluator -> runWithIsolation(evaluator, ctx, timeout),
            concurrency)
        .doOnComplete(() -> log.info("[{}] All domain workers completed",
            ctx.scanRequest().scanId()))
        .doOnError(e -> log.error("[{}] Dispatch error: {}",
            ctx.scanRequest().scanId(), e.getMessage()));
  }

  // ── Internal Helpers ───────────────────────────────────────────────────────

  /**
   * Filters evaluators based on: scan profile enablement, rule catalog enablement,
   * and version gate resolution against the detected project framework versions.
   */
  private List<RuleEvaluator> activeEvaluators(DomainContext ctx) {
    var profile = ctx.scanRequest().scanProfile();
    var astIndex = ctx.astIndex();

    return ruleEvaluators.stream()
        .filter(e -> !profile.isRuleDisabled(e.ruleId()))
        .filter(e -> catalogLoader.findById(e.ruleId())
            .map(rule -> {
              if (!rule.enabled()) return false;
              if (!profile.isDomainEnabled(rule.domain())) return false;
              if (rule.isVersionGated()) {
                String detected = astIndex.frameworkVersion(rule.sinceFramework());
                return meetsVersionGate(detected, rule.sinceVersion());
              }
              return true;
            })
            .orElse(true)) // Unknown rules: allow by default
        .toList();
  }

  /**
   * Runs a single rule evaluator with timeout + error isolation.
   * An evaluator that times out or throws emits 0 findings (never propagates error).
   */
  private Flux<Finding> runWithIsolation(RuleEvaluator evaluator, DomainContext ctx,
                                         Duration timeout) {
    return evaluator.evaluate(ctx)
        .subscribeOn(Schedulers.boundedElastic())
        .timeout(timeout)
        .doOnNext(f -> log.debug("[{}] Finding: {} @ {}:{}", ctx.scanRequest().scanId(),
            f.ruleId(), f.file(), f.line()))
        .onErrorResume(TimeoutException.class, e -> {
          log.warn("[{}] Evaluator {} timed out after {}",
              ctx.scanRequest().scanId(), evaluator.ruleId(), timeout);
          return Flux.empty();
        })
        .onErrorResume(e -> {
          log.error("[{}] Evaluator {} failed: {}",
              ctx.scanRequest().scanId(), evaluator.ruleId(), e.getMessage());
          return Flux.empty();
        });
  }

  /**
   * Semantic version comparison: returns {@code true} if {@code detected >= required}.
   * Supports "major.minor.patch" format. Missing parts are treated as 0.
   */
  private boolean meetsVersionGate(String detected, String required) {
    try {
      int[] d = parseVersion(detected);
      int[] r = parseVersion(required);
      for (int i = 0; i < 3; i++) {
        if (d[i] > r[i]) return true;
        if (d[i] < r[i]) return false;
      }
      return true; // equal
    } catch (Exception e) {
      log.warn("Version comparison failed ({} vs {}): {}", detected, required, e.getMessage());
      return false;
    }
  }

  private int[] parseVersion(String v) {
    var parts = (v != null ? v : "0").split("\\.");
    int[] result = {0, 0, 0};
    for (int i = 0; i < Math.min(3, parts.length); i++) {
      try { result[i] = Integer.parseInt(parts[i].replaceAll("[^0-9]", "")); }
      catch (NumberFormatException ignored) {}
    }
    return result;
  }
}
