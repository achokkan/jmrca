package com.jmcra.annotations;

import com.jmcra.model.Severity;

import java.lang.annotation.*;

/**
 * Marks a {@code static final String} field in a {@link RuleContractTest} class
 * as a positive fixture — a code snippet that MUST produce at least one finding
 * matching the specified severity and minimum confidence.
 *
 * <h3>Contract (SDD Section 7.2)</h3>
 * "Each rule MUST have at least one SHOULD_FIND test case."
 * The test runner (JUnit 5 extension or parameterised test) will:
 * <ol>
 *   <li>Parse the field value as a Java code snippet.</li>
 *   <li>Pass it to the {@code RuleEvaluator.evaluate()} method.</li>
 *   <li>Assert that at least one {@code Finding} is returned.</li>
 *   <li>Assert that the finding's severity matches and confidence ≥ minConfidence.</li>
 * </ol>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShouldFind {

  /** Expected severity of the produced finding. */
  Severity severity();

  /**
   * Minimum acceptable confidence score for the produced finding.
   * Corresponds to the {@code confidence} field in FindingSchema v1.
   * LLM-augmented findings should have confidence ≥ 0.5;
   * pure AST-deterministic findings should have confidence ≥ 0.85.
   */
  double minConfidence() default 0.5;
}
