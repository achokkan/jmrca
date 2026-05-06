package com.jmcra.rules.concurrent;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for CON-011: Structured concurrency (JEP 505) task scope not closed in try-with-resources
 * SPC-105: CON-011 Structured concurrency safety
 */
@RuleContractTest(specClause = "SPC-105", ruleId = "CON-011")
class StructuredConcurrencyContractTest {

  @ShouldFind(severity = Severity.MEDIUM, minConfidence = 0.95)
  @SpecEvidence("StructuredTaskScope instantiated outside of a try-with-resources block leaks threads.")
  static final String UNCLOSED_SCOPE = """
      package com.acme;
      import java.util.concurrent.StructuredTaskScope;
      
      public class AsyncSvc {
          public void execute() {
              var scope = new StructuredTaskScope.ShutdownOnFailure();
              scope.fork(() -> "task");
              scope.join();
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("try-with-resources guarantees scope closure and thread cleanup.")
  static final String TRY_WITH_RESOURCES_SCOPE = """
      package com.acme;
      import java.util.concurrent.StructuredTaskScope;
      
      public class AsyncSvc {
          public void execute() throws InterruptedException {
              try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                  scope.fork(() -> "task");
                  scope.join();
              }
          }
      }
      """;
}
