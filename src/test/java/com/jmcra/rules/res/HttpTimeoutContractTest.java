package com.jmcra.rules.res;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for RES-002: Http client timeouts configured
 */
@RuleContractTest(specClause = "SPC-101", ruleId = "RES-002")
class HttpTimeoutContractTest {

  @ShouldFind(severity = Severity.HIGH, minConfidence = 0.90)
  @SpecEvidence("RestTemplate default constructor leaves timeouts infinite")
  static final String RESTTEMPLATE_NO_TIMEOUT = """
      package com.acme;
      import org.springframework.web.client.RestTemplate;
      
      public class HttpConfig {
          public RestTemplate getRestTemplate() {
              return new RestTemplate();
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("RestTemplate with setConnectTimeout is safe")
  static final String RESTTEMPLATE_WITH_TIMEOUT = """
      package com.acme;
      import org.springframework.web.client.RestTemplate;
      import org.springframework.boot.web.client.RestTemplateBuilder;
      import java.time.Duration;
      
      public class HttpConfig {
          public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
              return builder
                  .setConnectTimeout(Duration.ofSeconds(5))
                  .setReadTimeout(Duration.ofSeconds(5))
                  .build();
          }
      }
      """;
}
