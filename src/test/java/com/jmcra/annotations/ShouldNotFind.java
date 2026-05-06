package com.jmcra.annotations;

import java.lang.annotation.*;

/**
 * Marks a {@code static final String} field in a {@link RuleContractTest} class
 * as a negative fixture — a code snippet that MUST NOT produce any finding.
 *
 * <h3>Contract (SDD Section 7.2)</h3>
 * "Each rule MUST have at least one SHOULD_NOT_FIND test case."
 * Negative fixtures are equally important to positive fixtures: they define
 * the precision boundary of the rule and prevent false positives.
 *
 * <h3>Example</h3>
 * <pre>{@code
 * @ShouldNotFind
 * @SpecEvidence("@Value injection is the correct externalisation pattern — not a credential literal")
 * static final String SPRING_VALUE =
 *     "@Value(\"${api.key}\") private String apiKey;";
 * }</pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShouldNotFind {
  // Marker annotation — no attributes required.
}
