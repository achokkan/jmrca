package com.jmcra.pipeline.stage6;

import com.jmcra.model.DeliveryResult;
import com.jmcra.model.FindingsReport;
import reactor.core.publisher.Mono;

/**
 * Contract for all Stage 6 delivery targets.
 * Each implementation routes the {@link FindingsReport} to a specific downstream system.
 *
 * <h3>Implementations</h3>
 * <ul>
 *   <li>{@link GitHubDelivery} — inline PR comments + Github check run.</li>
 *   <li>{@link JiraDelivery}   — JIRA sub-task creation for P0/P1 findings.</li>
 *   <li>{@link SlackDelivery}  — summary card with top-N findings.</li>
 * </ul>
 *
 * Spec: Section 4.2 (Stage 6 — Deliver):
 * "All targets receive the same FindingsReport payload."
 */
public interface DeliveryTarget {

  /** Unique name for this delivery target (e.g., "github", "jira", "slack"). */
  String name();

  /** Returns {@code true} if this target is enabled in the current configuration. */
  boolean isEnabled();

  /**
   * Delivers the findings report to this target.
   *
   * @param report The final, ranked findings report from Stage 5.
   * @return A {@code Mono} emitting a {@link DeliveryResult} indicating success/failure.
   *         Errors should be recovered internally and returned as a failed {@link DeliveryResult}.
   */
  Mono<DeliveryResult> deliver(FindingsReport report);
}
