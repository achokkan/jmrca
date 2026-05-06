package com.jmcra.rules.api;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for API-002: Pageable endpoint missing max-size guard
 * SPC-107: API-002 Pageable boundaries
 */
@RuleContractTest(specClause = "SPC-107", ruleId = "API-002")
class PageableBoundsContractTest {

  @ShouldFind(severity = Severity.MEDIUM, minConfidence = 0.90)
  @SpecEvidence("A Pageable parameter lacking @PageableDefault leaves pagination entirely up to the client, exposing DB exhaustion risks.")
  static final String UNBOUNDED_PAGEABLE = """
      package com.acme;
      import org.springframework.web.bind.annotation.RestController;
      import org.springframework.web.bind.annotation.GetMapping;
      import org.springframework.data.domain.Pageable;
      
      @RestController
      public class UserController {
          @GetMapping("/users")
          public String getUsers(Pageable pageable) {
              return "users";
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("@PageableDefault enforces hard maximums preventing DB abuse.")
  static final String BOUNDED_PAGEABLE = """
      package com.acme;
      import org.springframework.web.bind.annotation.RestController;
      import org.springframework.web.bind.annotation.GetMapping;
      import org.springframework.data.web.PageableDefault;
      import org.springframework.data.domain.Pageable;
      
      @RestController
      public class UserController {
          @GetMapping("/users")
          public String getUsers(@PageableDefault(size = 20) Pageable pageable) {
              return "users";
          }
      }
      """;
}
