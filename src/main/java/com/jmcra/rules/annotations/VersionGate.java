package com.jmcra.rules.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the minimum framework version required for this rule to be dispatched.
 * The {@code AnalysisDispatchService} reads the project's build file to detect
 * the framework version and skips rules whose version gate is not met.
 * <p>
 * Example:
 * <pre>{@code
 * @RuleDefinition(
 *   id          = "API-005",
 *   domain      = Domain.API,
 *   severity    = Severity.LOW,
 *   sinceVersion = @VersionGate(framework = Framework.SPRING_BOOT, minVersion = "4.0.0")
 * )
 * }</pre>
 * <p>
 * Spec: Section 2A.3 (Version Compatibility Matrix for Rule Evaluation).
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VersionGate {

  /** The framework this version gate applies to. */
  Framework framework();

  /**
   * The minimum version string that must be present in the project's build file
   * for this rule to be active. Uses semantic version comparison (major.minor.patch).
   */
  String minVersion();
}
