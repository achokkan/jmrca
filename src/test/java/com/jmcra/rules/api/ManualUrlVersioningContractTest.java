package com.jmcra.rules.api;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for API-005: Manual URL-prefix versioning detected when native API versioning is available.
 * SPC-104: API-005 Spring Boot 4 versioning 
 */
@RuleContractTest(specClause = "SPC-104", ruleId = "API-005")
class ManualUrlVersioningContractTest {

  @ShouldFind(severity = Severity.LOW, minConfidence = 0.90)
  @SpecEvidence("Manual string /v1/ or /v2/ in @RequestMapping violates native versioning routing.")
  static final String MANUAL_V1 = """
      package com.acme;
      import org.springframework.web.bind.annotation.RequestMapping;
      import org.springframework.web.bind.annotation.RestController;
      
      @RestController
      @RequestMapping("/v1/users")
      public class UserController {
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Paths without manual versions are safe")
  static final String NATIVE_VERSIONING = """
      package com.acme;
      import org.springframework.web.bind.annotation.RequestMapping;
      import org.springframework.web.bind.annotation.RestController;
      import org.springframework.web.bind.annotation.ApiVersion;
      
      @RestController
      @RequestMapping("/users")
      @ApiVersion("1.0")
      public class UserController {
      }
      """;
}
