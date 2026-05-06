package com.jmcra.rules.api;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for API-001: REST endpoint returns HTTP 500 on known business exception.
 * SPC-106: API-001 exception handling bounds
 */
@RuleContractTest(specClause = "SPC-106", ruleId = "API-001")
class Http500HandlerContractTest {

  @ShouldFind(severity = Severity.HIGH, minConfidence = 0.85)
  @SpecEvidence("A thrown runtime exception from a RestController without an ExceptionHandler leaks 500s and stack traces.")
  static final String UNHANDLED_EXCEPTION = """
      package com.acme;
      import org.springframework.web.bind.annotation.RestController;
      import org.springframework.web.bind.annotation.GetMapping;
      
      @RestController
      public class OrderController {
          
          @GetMapping("/orders")
          public String getOrders() {
              throw new IllegalStateException("Database is down");
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("@ExceptionHandler catches the exception and returns a graceful 400 or structured 500.")
  static final String HANDLED_EXCEPTION = """
      package com.acme;
      import org.springframework.web.bind.annotation.RestController;
      import org.springframework.web.bind.annotation.GetMapping;
      import org.springframework.web.bind.annotation.ExceptionHandler;
      
      @RestController
      public class OrderController {
          
          @GetMapping("/orders")
          public String getOrders() {
              throw new IllegalStateException("Database is down");
          }
          
          @ExceptionHandler(IllegalStateException.class)
          public String handle(IllegalStateException ex) {
              return "error";
          }
      }
      """;
}
