package com.jmcra.rules.obs;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for OBS-003: Custom metric name does not follow Prometheus naming convention
 * SPC-103: OBS-003 metrics rules
 */
@RuleContractTest(specClause = "SPC-103", ruleId = "OBS-003")
class MetricNamingContractTest {

  @ShouldFind(severity = Severity.MEDIUM, minConfidence = 0.90)
  @SpecEvidence("Camel case metric names violate Prometheus snake_case standards.")
  static final String CAMEL_CASE_METRIC = """
      package com.acme;
      import io.micrometer.core.instrument.MeterRegistry;
      
      public class TelemetrySvc {
          public TelemetrySvc(MeterRegistry registry) {
              registry.counter("userLoginsTotal").increment();
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Snake case is fully compliant.")
  static final String SNAKE_CASE_METRIC = """
      package com.acme;
      import io.micrometer.core.instrument.MeterRegistry;
      
      public class TelemetrySvc {
          public TelemetrySvc(MeterRegistry registry) {
              registry.counter("user_logins_total").increment();
          }
      }
      """;
}
