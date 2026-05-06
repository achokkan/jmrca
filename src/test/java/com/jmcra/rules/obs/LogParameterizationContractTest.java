package com.jmcra.rules.obs;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for OBS-002: Log statement uses string concatenation instead of parameterised (SLF4J) style
 * SPC-100: OBS-002 log parameters positive/negative cases
 */
@RuleContractTest(specClause = "SPC-100", ruleId = "OBS-002")
class LogParameterizationContractTest {

  @ShouldFind(severity = Severity.MEDIUM, minConfidence = 0.95)
  @SpecEvidence("String concatenation causes unnecessary memory allocation on ignored log levels.")
  static final String LOG_CONCAT = """
      package com.acme;
      import org.slf4j.Logger;
      import org.slf4j.LoggerFactory;
      
      public class AuditService {
          private static final Logger log = LoggerFactory.getLogger(AuditService.class);
          
          public void audit(String id) {
              log.info("Auditing item id: " + id);
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("SLF4J {} parameterization defers string building.")
  static final String LOG_PARAMETERIZED = """
      package com.acme;
      import org.slf4j.Logger;
      import org.slf4j.LoggerFactory;
      
      public class AuditService {
          private static final Logger log = LoggerFactory.getLogger(AuditService.class);
          
          public void audit(String id) {
              log.info("Auditing item id: {}", id);
          }
      }
      """;
}
