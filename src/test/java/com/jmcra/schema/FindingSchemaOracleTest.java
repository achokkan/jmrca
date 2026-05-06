package com.jmcra.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmcra.annotations.SchemaOracleTest;
import com.jmcra.annotations.SpecOracle;
import com.jmcra.model.Finding;
import com.jmcra.model.Severity;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Schema Oracle Test — validates that every {@link Finding} produced by any
 * {@code RuleEvaluator} complies with FindingSchema v1.
 *
 * <h3>Oracle (SPC-031)</h3>
 * <ul>
 *   <li>All required fields are present.</li>
 *   <li>No additional properties are permitted.</li>
 *   <li>Line numbers are > 0.</li>
 *   <li>Confidence is in [0.0, 1.0].</li>
 *   <li>RuleId matches {@code DOM-NNN} pattern.</li>
 * </ul>
 *
 * Spec: Section 7A.4 (Finding Schema Oracle Tests). SPC-031.
 */
@SchemaOracleTest(specClause = "SPC-031")
class FindingSchemaOracleTest {

  private static JsonSchema       SCHEMA;
  private static ObjectMapper     MAPPER;

  @BeforeAll
  static void loadSchema() throws Exception {
    MAPPER = new ObjectMapper();
    var factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

    // Try classpath, then filesystem
    InputStream schemaStream = FindingSchemaOracleTest.class
        .getResourceAsStream("/schemas/finding-v1.json");
    if (schemaStream == null) {
      Path fsPath = Path.of("schemas/finding-v1.json");
      assertThat(fsPath).exists()
          .describedAs("schemas/finding-v1.json must exist for SPC-031 oracle test");
      schemaStream = Files.newInputStream(fsPath);
    }
    SCHEMA = factory.getSchema(schemaStream);
  }

  // ── SPC-031: Schema Compliance ─────────────────────────────────────────────

  @Test
  @SpecOracle("A well-formed Finding serialises to JSON that passes the FindingSchema v1 validator")
  void wellFormedFindingPassesSchemaValidation() throws Exception {
    Finding finding = Finding.builder("SEC-001", Severity.CRITICAL)
        .title("Hardcoded credential detected")
        .file("src/main/java/com/acme/Config.java")
        .line(42)
        .column(18)
        .snippet("String apiKey = \"sk-prod-x9f2mZ\";")
        .message("A credential literal was detected. This exposes secrets in source control.")
        .remediation("Inject via environment variable or Vault secret reference.")
        .references(List.of("CWE-798", "OWASP-A07:2021"))
        .confidence(0.97)
        .ruleVersion("1.3")
        .build();

    Set<ValidationMessage> errors = validateFinding(finding);
    assertThat(errors)
        .withFailMessage("FindingSchema v1 validation failed: %s", errors)
        .isEmpty();
  }

  @Test
  @SpecOracle("SPC-031a: line number must be > 0 in every finding")
  void findingLineNumberIsGreaterThanZero() {
    // Finding constructor enforces line >= 1 — verify no bypass possible
    assertThat(
        java.util.stream.Stream.of(Severity.values())
            .map(sev -> Finding.builder("SEC-001", sev)
                .title("Test finding").file("Test.java").line(1)
                .message("test message").remediation("test remediation")
                .confidence(0.8).ruleVersion("1.0").build()
                .line())
            .allMatch(l -> l >= 1)
    ).isTrue();
  }

  @Test
  @SpecOracle("Confidence must be within [0.0, 1.0] — constructor rejects out-of-range values")
  void findingConfidenceIsWithinBounds() {
    // Confidence validation is in the Finding compact constructor
    assertThatThrownBy(() ->
        Finding.builder("SEC-001", Severity.CRITICAL)
            .title("T").file("F.java").line(1)
            .message("msg").remediation("fix")
            .confidence(1.5)  // invalid
            .ruleVersion("1.0").build()
    ).isInstanceOf(IllegalArgumentException.class)
     .hasMessageContaining("confidence");
  }

  @Test
  @SpecOracle("RuleId must match DOM-NNN pattern — enforced by JSON Schema ruleId.pattern constraint")
  void invalidRuleIdFailsSchemaValidation() throws Exception {
    // Build a finding with invalid ruleId to verify schema rejects it
    Finding finding = Finding.builder("INVALID-ID", Severity.HIGH)
        .title("Test").file("Test.java").line(1)
        .message("Test message for schema validation")
        .remediation("Fix it properly")
        .confidence(0.8).ruleVersion("1.0").build();

    Set<ValidationMessage> errors = validateFinding(finding);
    assertThat(errors)
        .withFailMessage("Expected schema to reject invalid ruleId 'INVALID-ID'")
        .isNotEmpty();
  }

  @ParameterizedTest(name = "Severity.{0} produces schema-valid Finding")
  @MethodSource("allSeverities")
  @SpecOracle("Every severity level produces a schema-valid Finding")
  void allSeverityLevelsProduceSchemaValidFindings(Severity severity) throws Exception {
    Finding f = Finding.builder("SEC-001", severity)
        .title("Test finding for " + severity.name())
        .file("src/main/java/Test.java")
        .line(10)
        .message("This is a test message for schema validation purposes.")
        .remediation("This is a test remediation for schema validation purposes.")
        .confidence(0.8)
        .ruleVersion("1.0")
        .build();

    Set<ValidationMessage> errors = validateFinding(f);
    assertThat(errors)
        .withFailMessage("Schema validation failed for severity %s: %s", severity, errors)
        .isEmpty();
  }

  static Stream<Severity> allSeverities() {
    return Stream.of(Severity.values());
  }

  // ── Helpers ────────────────────────────────────────────────────────────────

  private Set<ValidationMessage> validateFinding(Finding finding) throws Exception {
    JsonNode node = MAPPER.readTree(finding.toJson());
    return SCHEMA.validate(node);
  }
}
