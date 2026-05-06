package com.jmcra.annotations;

import java.lang.annotation.*;

/**
 * Meta-annotation for Finding Schema oracle tests.
 * Applied to the {@code FindingSchemaOracleTest} class (SPC-031).
 *
 * <h3>Oracle (SPC-031)</h3>
 * "Every Finding produced by any RuleEvaluator must validate against
 *  FindingSchema v1 (JSON Schema, stored in schemas/finding-v1.json).
 *  No additional properties permitted. All required fields present."
 *
 * Spec: Section 7A.4 (Finding Schema Oracle Tests).
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@org.junit.jupiter.api.Tag("spec-contract")
public @interface SchemaOracleTest {

  /** The Spec Clause ID (e.g., "SPC-031") covered by this schema oracle test. */
  String specClause();
}
