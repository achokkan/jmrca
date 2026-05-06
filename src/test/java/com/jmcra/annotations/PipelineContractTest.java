package com.jmcra.annotations;

import java.lang.annotation.*;

/**
 * Meta-annotation for JMCRA pipeline stage contract tests.
 * One class per pipeline stage (Stage 1 through Stage 6).
 * <p>
 * Spec: Section 7A.3 (Pipeline Stage Contract Tests).
 * "Each of the six pipeline stages has a contract test that validates its input/output boundary."
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@org.junit.jupiter.api.Tag("spec-contract")
public @interface PipelineContractTest {

  /** The Spec Clause ID(s) covered by this pipeline stage test. */
  String specClause();

  /** The pipeline stage this test validates. */
  PipelineStage stage();
}
