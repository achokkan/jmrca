package com.jmcra.annotations;

import java.lang.annotation.*;

/**
 * Documents the rationale for a test fixture, linking code snippets to the
 * specific spec behaviour they exercise.
 *
 * <h3>Usage</h3>
 * Apply to {@code @ShouldFind} or {@code @ShouldNotFind} fields to document
 * <em>why</em> the snippet should or should not trigger a finding.
 * This annotation is mandatory for all fixtures to maintain traceability
 * from test code back to the specification.
 *
 * <pre>{@code
 * @ShouldFind(severity = CRITICAL, minConfidence = 0.90)
 * @SpecEvidence("String literal with entropy > 3.5 bits/char — matches SEC-001 detection signal")
 * static final String INLINE_SECRET =
 *     "private static final String API_KEY = \"sk-prod-x9f2mZ\";";
 * }</pre>
 *
 * Spec: Section 7A.2 (Executable Rule Contract Tests).
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpecEvidence {

  /** Human-readable explanation of why this fixture exercises the specified spec behaviour. */
  String value();
}
