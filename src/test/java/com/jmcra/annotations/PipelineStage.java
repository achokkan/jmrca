package com.jmcra.annotations;

/**
 * Enumeration of the six JMCRA pipeline stages for use in {@link PipelineContractTest}.
 * <p>
 * Spec: Section 4.2 (Agent Architecture — Six-Stage Pipeline).
 */
public enum PipelineStage {

  /** Stage 1: WebhookPayload → ScanRequest. Validates HMAC + shape. */
  INGEST,

  /** Stage 2: ScanRequest → DomainContext (AST index + dep tree + config). */
  PARSE_INDEX,

  /** Stage 3: DomainContext → parallel domain worker dispatch. */
  ANALYSIS_DISPATCH,

  /** Stage 4: Embedded within Stage 3 — RuleEvaluator.evaluate(ctx). */
  RULE_EVALUATORS,

  /** Stage 5: List&lt;Finding&gt; → ranked, deduplicated FindingsReport. */
  RANK_DEDUPE,

  /** Stage 6: FindingsReport → all configured delivery targets. */
  DELIVER
}
