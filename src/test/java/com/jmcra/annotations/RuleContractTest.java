package com.jmcra.annotations;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.*;

/**
 * Meta-annotation for JMCRA rule contract tests.
 *
 * <h3>SDD Mandate (Section 7A.2)</h3>
 * Every rule evaluator MUST have a class annotated with {@code @RuleContractTest}.
 * The {@code specClause} links the test back to a Spec Clause ID (SPC-NNN) in the
 * living traceability matrix ({@code docs/traceability-matrix.csv}).
 *
 * <h3>Usage</h3>
 * <pre>{@code
 * @RuleContractTest(specClause = "SPC-042", ruleId = "SEC-001")
 * class HardcodedCredentialContractTest {
 *
 *   @ShouldFind(severity = CRITICAL, minConfidence = 0.90)
 *   @SpecEvidence("String literal with entropy > 3.5 bits/char")
 *   static final String INLINE_SECRET =
 *       "private static final String API_KEY = \"sk-prod-x9f2mZ\";";
 *
 *   @ShouldNotFind
 *   @SpecEvidence("@Value injection — correct externalisation pattern")
 *   static final String SPRING_VALUE =
 *       "@Value(\"${api.key}\") private String apiKey;";
 * }
 * }</pre>
 *
 * Spec: Section 7.2 (Acceptance Test Format), Section 7A.2 (Executable Rule Contract Tests).
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Tag("spec-contract")
public @interface RuleContractTest {

  /**
   * The Spec Clause ID (e.g., "SPC-042") that this test covers.
   * Used by {@code scripts/check-traceability.sh} to build the traceability matrix.
   * Multiple clauses may be comma-separated: "SPC-042,SPC-043".
   */
  String specClause();

  /**
   * The stable rule ID this test covers (e.g., "SEC-001").
   * Must match an entry in {@code rules/catalog.json}.
   */
  String ruleId();
}
