package com.jmcra.pipeline.stage4;

import com.jmcra.model.Finding;
import reactor.core.publisher.Flux;

/**
 * Contract for every rule evaluation function in JMCRA.
 *
 * <h3>Implementation Rules (SDD Section 4.2 — Stage 4)</h3>
 * <ul>
 *   <li>Rule evaluation functions MUST be <strong>pure</strong>: same input always produces same output.</li>
 *   <li>Implementations MUST be annotated with {@link com.jmcra.rules.annotations.RuleDefinition}.</li>
 *   <li>Each implementation MUST have a corresponding {@code @RuleContractTest} in {@code tests/rules/}.</li>
 *   <li>CRITICAL severity findings MUST have deterministic AST evidence — LLM is never the sole authority.</li>
 * </ul>
 *
 * <h3>Minimal Implementation Template</h3>
 * <pre>{@code
 * @RuleDefinition(id = "SEC-001", domain = Domain.SEC, severity = Severity.CRITICAL)
 * @Component
 * public class HardcodedCredentialRule implements RuleEvaluator {
 *
 *   @Override public String   ruleId()          { return "SEC-001"; }
 *   @Override public Severity defaultSeverity() { return Severity.CRITICAL; }
 *
 *   @Override
 *   public Flux<Finding> evaluate(DomainContext ctx) {
 *     return Flux.fromStream(
 *         ctx.astIndex().compilationUnits().values().stream()
 *            .flatMap(cu -> detectCredentials(cu).stream())
 *     );
 *   }
 * }
 * }</pre>
 *
 * Spec: Section 4.2 (Stage 4 — Rule Evaluators).
 */
public interface RuleEvaluator {

  /**
   * The stable rule ID for this evaluator (e.g., "SEC-001").
   * Must match the {@code id} in {@code rules/catalog.json} and the
   * {@code ruleId} on the class's {@code @RuleDefinition} annotation.
   */
  String ruleId();

  /**
   * The default severity produced by this rule.
   * Used by {@code RankDedupeService} when no per-finding severity override is set.
   */
  com.jmcra.model.Severity defaultSeverity();

  /**
   * Evaluates this rule against the given domain context.
   *
   * <p>The returned {@code Flux} must:
   * <ul>
   *   <li>Complete (not hang indefinitely).</li>
   *   <li>Emit {@code onComplete} (with 0 or more findings) for code that is clean.</li>
   *   <li>Never emit {@code onError} — handle errors internally and emit 0 findings.</li>
   *   <li>Be subscribable multiple times if the {@code Flux} is cold (which it should be).</li>
   * </ul>
   *
   * @param ctx The domain context containing the AST index, dependency tree, and config files.
   * @return A cold {@code Flux} of findings. Empty if no violation is detected.
   */
  Flux<Finding> evaluate(DomainContext ctx);
}
