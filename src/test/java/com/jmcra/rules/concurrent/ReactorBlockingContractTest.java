package com.jmcra.rules.concurrent;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for CON-001: Blocking call detected on Project Reactor thread.
 * SPC-112: CON-001 BlockHound equivalents
 */
@RuleContractTest(specClause = "SPC-112", ruleId = "CON-001")
class ReactorBlockingContractTest {

  @ShouldFind(severity = Severity.CRITICAL, minConfidence = 0.90)
  @SpecEvidence("A reactive chain mapping a blocking Thread.sleep acts as a thread starvation hazard.")
  static final String BLOCKING_SLEEP = """
      package com.acme;
      import reactor.core.publisher.Mono;
      
      public class AsyncSvc {
          public Mono<String> process() {
              return Mono.just("start").map(s -> {
                  try {
                      Thread.sleep(1000);
                  } catch (InterruptedException e) {}
                  return s;
              });
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Mono.delay uses non-blocking schedulers.")
  static final String NONBLOCKING_DELAY = """
      package com.acme;
      import reactor.core.publisher.Mono;
      import java.time.Duration;
      
      public class AsyncSvc {
          public Mono<String> process() {
              return Mono.just("start").delayElement(Duration.ofSeconds(1));
          }
      }
      """;
}
