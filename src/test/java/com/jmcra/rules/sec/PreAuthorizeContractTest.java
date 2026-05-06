package com.jmcra.rules.sec;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Contract Tests for SEC-004: Missing @PreAuthorize on public @RestController endpoints.
 * <p>
 * SPC-048: SEC-004 positive: missing @PreAuthorize
 * SPC-049: SEC-004 negative: @PreAuthorize present
 */
@RuleContractTest(specClause = "SPC-048,SPC-049", ruleId = "SEC-004")
class PreAuthorizeContractTest {

  @ShouldFind(severity = Severity.MEDIUM, minConfidence = 0.90)
  @SpecEvidence("Public @GetMapping missing authorization")
  static final String MISSING_AUTH = """
      package com.acme;
      import org.springframework.web.bind.annotation.RestController;
      import org.springframework.web.bind.annotation.GetMapping;
      
      @RestController
      public class ApiController {
          
          @GetMapping("/users")
          public String getUsers() {
              return "users";
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("@RestController method with @PreAuthorize")
  static final String WITH_AUTH = """
      package com.acme;
      import org.springframework.web.bind.annotation.RestController;
      import org.springframework.web.bind.annotation.GetMapping;
      import org.springframework.security.access.prepost.PreAuthorize;
      
      @RestController
      public class ApiController {
          
          @GetMapping("/users")
          @PreAuthorize("hasRole('ADMIN')")
          public String getUsers() {
              return "users";
          }
      }
      """;
      
  @ShouldNotFind
  @SpecEvidence("Private methods inside RestController do not need it")
  static final String PRIVATE_METHOD = """
      package com.acme;
      import org.springframework.web.bind.annotation.RestController;
      
      @RestController
      public class ApiController {
          
          private String computeSomething() {
              return "secure";
          }
      }
      """;
}
