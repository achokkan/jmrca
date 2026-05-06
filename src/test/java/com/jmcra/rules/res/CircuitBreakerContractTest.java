package com.jmcra.rules.res;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for RES-001: @FeignClient missing @CircuitBreaker
 * SPC-088: RES-001 circuit breaker absence
 */
@RuleContractTest(specClause = "SPC-088", ruleId = "RES-001")
class CircuitBreakerContractTest {

  @ShouldFind(severity = Severity.HIGH, minConfidence = 0.90)
  @SpecEvidence("Feign clients inherently imply remote RPC. They must fail-fast with CircuitBreakers.")
  static final String MISSING_CB = """
      package com.acme;
      import org.springframework.cloud.openfeign.FeignClient;
      import org.springframework.web.bind.annotation.GetMapping;
      
      @FeignClient(name = "user-service")
      public interface UserClient {
          @GetMapping("/users")
          String getUsers();
      }
      """;

  @ShouldNotFind
  @SpecEvidence("FallbackFactory configured handles resilience inside Feign core")
  static final String FEIGN_WITH_FALLBACK = """
      package com.acme;
      import org.springframework.cloud.openfeign.FeignClient;
      import org.springframework.web.bind.annotation.GetMapping;
      
      @FeignClient(name = "user-service", fallbackFactory = UserFallback.class)
      public interface UserClient {
          @GetMapping("/users")
          String getUsers();
      }
      """;
      
  @ShouldNotFind
  @SpecEvidence("Resilience4j CircuitBreaker wrapper is valid")
  static final String WITH_RESILIENCE4J = """
      package com.acme;
      import org.springframework.cloud.openfeign.FeignClient;
      import org.springframework.web.bind.annotation.GetMapping;
      import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
      
      @CircuitBreaker(name = "userService")
      @FeignClient(name = "user-service")
      public interface UserClient {
          @GetMapping("/users")
          String getUsers();
      }
      """;
}
