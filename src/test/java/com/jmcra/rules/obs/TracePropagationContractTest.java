package com.jmcra.rules.obs;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for OBS-001: Trace context not propagated across @FeignClient boundary
 * SPC-099: OBS-001 trace propagation Feign
 */
@RuleContractTest(specClause = "SPC-099", ruleId = "OBS-001")
class TracePropagationContractTest {

  @ShouldFind(severity = Severity.HIGH, minConfidence = 0.90)
  @SpecEvidence("Without traceparent header explicitly forwarded, trace chain is broken over HTTP.")
  static final String MISSING_TRACING = """
      package com.acme;
      import feign.RequestInterceptor;
      import feign.RequestTemplate;
      import org.springframework.context.annotation.Bean;
      
      public class FeignConfig {
          @Bean
          public RequestInterceptor interceptor() {
              return template -> {
                  template.header("Authorization", "Bearer token");
              };
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Providing trace headers manually or automatically fixes propagation.")
  static final String VALID_TRACING = """
      package com.acme;
      import feign.RequestInterceptor;
      import feign.RequestTemplate;
      import org.springframework.context.annotation.Bean;
      
      public class FeignConfig {
          @Bean
          public RequestInterceptor interceptor() {
              return template -> {
                  template.header("traceparent", "00-1234");
              };
          }
      }
      """;
}
