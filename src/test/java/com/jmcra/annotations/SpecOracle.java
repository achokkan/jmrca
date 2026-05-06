package com.jmcra.annotations;

import java.lang.annotation.*;

/**
 * Documents the oracle condition for a pipeline stage contract test method.
 * Used on {@code @Test} methods inside {@link PipelineContractTest} classes
 * to explicitly state the pass/fail condition being verified.
 *
 * <pre>{@code
 * @Test
 * @SpecOracle("HMAC validation — invalid signature → 401 Unauthorized")
 * void rejectsInvalidHmacSignature() { ... }
 * }</pre>
 *
 * Spec: Section 7A.1 (Five-Step Spec-to-Test Checklist — Step 2: Define the Oracle).
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpecOracle {

  /** The precise pass/fail condition this test method verifies. */
  String value();
}
