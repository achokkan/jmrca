package com.jmcra.model;

import java.time.Instant;

/**
 * Internal event emitted on the reactive event bus after a successful Stage 1 ingest.
 * Downstream components can subscribe to this event using {@code Sinks.Many}.
 * <p>
 * Spec: Section 4.2 (Stage 1 — Ingest): "Emits ScanStarted event on the internal bus."
 *
 * @param scanRequest The normalised scan request that triggered this event.
 * @param emittedAt   Timestamp when the event was emitted.
 */
public record ScanStarted(
    ScanRequest scanRequest,
    Instant emittedAt
) {

  public ScanStarted(ScanRequest scanRequest) {
    this(scanRequest, Instant.now());
  }

  public String scanId() {
    return scanRequest.scanId();
  }
}
