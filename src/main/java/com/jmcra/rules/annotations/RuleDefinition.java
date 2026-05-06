package com.jmcra.rules.annotations;

import com.jmcra.model.Domain;
import com.jmcra.model.Severity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a class as a JMCRA rule evaluator. Every class annotated with
 * {@code @RuleDefinition} must implement {@link com.jmcra.pipeline.stage4.RuleEvaluator}.
 * <p>
 * The rule ID (e.g., "SEC-001") is stable and never changes once published.
 * Changes to ID, description, or defaultSeverity require a CHANGELOG entry
 * to pass the {@code catalog-integrity-check} CI gate.
 * <p>
 * Spec: Section 5 (Rule Catalog), Section 2A.3 (Version Compatibility Matrix).
 *
 * <pre>{@code
 * @RuleDefinition(
 *   id          = "SEC-001",
 *   domain      = Domain.SEC,
 *   severity    = Severity.CRITICAL,
 *   description = "Hardcoded credential or API key literal in source"
 * )
 * public class HardcodedCredentialRule implements RuleEvaluator { ... }
 * }</pre>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RuleDefinition {

  /**
   * Stable rule identifier in DOM-NNN format (e.g., "SEC-001").
   * Must match an entry in {@code rules/catalog.json}.
   */
  String id();

  /** The review domain this rule belongs to. */
  Domain domain();

  /** Default severity; may be overridden per-team via {@code .jmcra/suppressions.yml}. */
  Severity severity();

  /** 
   * The explicit version of this rule evaluator implementation.
   * Tracks iterative improvements to the detection logic (e.g., 1.0 -> 1.1).
   */
  String version() default "1.0";

  /** Brief human-readable description of what this rule detects. */
  String description() default "";

  /**
   * Minimum framework version required for this rule to be dispatched.
   * Defaults to Java 17 (unconditional for all spec-supported Java versions).
   */
  VersionGate sinceVersion() default @VersionGate(
      framework = Framework.JAVA,
      minVersion = "17"
  );
}
