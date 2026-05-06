package com.jmcra.rules.dep;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for DEP-005: javax.inject import detected
 * SPC-072: DEP-005 javax.inject import flagged
 */
@RuleContractTest(specClause = "SPC-072", ruleId = "DEP-005")
class JavaxInjectContractTest {

  @ShouldFind(severity = Severity.MEDIUM, minConfidence = 1.0)
  @SpecEvidence("javax.inject namespace is deprecated as of Jakarta EE 9 / Spring Boot 3")
  static final String JAVAX_INJECT = """
      package com.acme;
      import javax.inject.Inject;
      
      public class MyService {
          @Inject
          private Object dependency;
      }
      """;

  @ShouldNotFind
  @SpecEvidence("jakarta.inject namespace is correct")
  static final String JAKARTA_INJECT = """
      package com.acme;
      import jakarta.inject.Inject;
      
      public class MyService {
          @Inject
          private Object dependency;
      }
      """;
}
