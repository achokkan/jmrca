package com.jmcra.model;

/**
 * The outcome of a single delivery target execution in Stage 6.
 * <p>
 * Spec: Section 4.2 (Stage 6 — Deliver):
 * "All targets receive the same FindingsReport payload."
 *
 * @param targetName  Name of the delivery target (e.g., "github", "jira", "slack").
 * @param success     Whether the delivery completed successfully.
 * @param message     Human-readable status or error description.
 * @param externalId  External reference ID created by the target (e.g., JIRA issue key,
 *                    GitHub check-run ID). {@code null} if not applicable or if failed.
 */
public record DeliveryResult(
    String  targetName,
    boolean success,
    String  message,
    String  externalId
) {

  public static DeliveryResult success(String target, String message, String externalId) {
    return new DeliveryResult(target, true, message, externalId);
  }

  public static DeliveryResult failure(String target, String message) {
    return new DeliveryResult(target, false, message, null);
  }
}
