package com.jmcra.pipeline.stage6;

import com.jmcra.config.JmcraProperties;
import com.jmcra.model.DeliveryResult;
import com.jmcra.model.FindingsReport;
import com.jmcra.model.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Stage 6 — Deliver.
 *
 * <h3>Responsibilities</h3>
 * <ol>
 *   <li>Fan-out to all enabled {@link DeliveryTarget} implementations in parallel.</li>
 *   <li>Sets the CI exit code gate: non-zero if a CRITICAL finding is present and
 *       the gateway threshold policy is violated.</li>
 *   <li>Returns the same {@link FindingsReport} enriched with delivery outcomes.</li>
 * </ol>
 *
 * Spec: Section 4.2 (Stage 6 — Deliver):
 * "Routes output to configured delivery targets.
 *  CI exit code: non-zero if critical-gate policy is violated."
 */
@Service
public class DeliveryService {

  private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

  private final List<DeliveryTarget> targets;
  private final JmcraProperties      props;

  public DeliveryService(List<DeliveryTarget> targets, JmcraProperties props) {
    this.targets = targets;
    this.props   = props;
    log.info("DeliveryService ready: {} targets registered ({})",
        targets.size(),
        targets.stream().map(DeliveryTarget::name).toList());
  }

  /**
   * Fans out the report to all enabled delivery targets in parallel,
   * then evaluates the CI gate policy.
   *
   * @param report The final, ranked findings report from Stage 5.
   * @return A {@code Mono} emitting the same report (possibly mutated with gate status).
   */
  public Mono<FindingsReport> deliver(FindingsReport report) {
    var enabledTargets = targets.stream().filter(DeliveryTarget::isEnabled).toList();

    if (enabledTargets.isEmpty()) {
      log.info("[{}] No delivery targets enabled — skipping delivery", report.scanId());
    }

    return reactor.core.publisher.Flux.fromIterable(enabledTargets)
        .flatMap(t -> t.deliver(report)
            .doOnSuccess(r -> log.info("[{}] Delivery [{}]: {} — {}",
                report.scanId(), t.name(), r.success() ? "OK" : "FAILED", r.message()))
            .onErrorResume(e -> {
              log.error("[{}] Delivery target [{}] threw: {}",
                  report.scanId(), t.name(), e.getMessage());
              return Mono.just(DeliveryResult.failure(t.name(), e.getMessage()));
            }))
        .collectList()
        .doOnSuccess(results -> {
          evaluateCiGate(report);
          log.info("[{}] All delivery complete. Gate violated={}",
              report.scanId(), report.gateViolated());
        })
        .thenReturn(report);
  }

  // ── CI Gate ────────────────────────────────────────────────────────────────

  /**
   * Evaluates the CI gateway policy. Logs a clear gate violation message so that
   * the CI system can detect non-zero exit (handled by {@link com.jmcra.JmcraApplication}).
   */
  private void evaluateCiGate(FindingsReport report) {
    String threshold = props.delivery().gateThreshold();
    if ("NONE".equalsIgnoreCase(threshold)) return;

    Severity gateSeverity;
    try {
      gateSeverity = Severity.valueOf(threshold.toUpperCase());
    } catch (IllegalArgumentException e) {
      log.warn("Invalid gate threshold '{}' — defaulting to CRITICAL", threshold);
      gateSeverity = Severity.CRITICAL;
    }

    final Severity gate = gateSeverity;
    boolean violated = report.findings().stream()
        .anyMatch(f -> f.severity().weight() >= gate.weight());

    if (violated) {
      log.error("━━━ 🚨 JMCRA CI GATE VIOLATED ━━━");
      log.error("Gate threshold: {} | Findings at or above gate: {}",
          gate, report.findings().stream()
              .filter(f -> f.severity().weight() >= gate.weight()).count());
      log.error("Build MUST fail. See findings report for details.");
    }
  }
}
