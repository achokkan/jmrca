package com.jmcra.rules.sec;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Contract Tests for SEC-001: Hardcoded credential or API key literal in source.
 * <p>
 * Spec: Section 3 (Security Domain)
 * SPC-042: SEC-001 positive: inline secret detection
 * SPC-043: SEC-001 negative: @Value injection not flagged
 */
@RuleContractTest(specClause = "SPC-042,SPC-043", ruleId = "SEC-001")
class HardcodedCredentialContractTest {

  @ShouldFind(severity = Severity.CRITICAL, minConfidence = 0.90)
  @SpecEvidence("String literal with entropy > 3.5 bits/char matching common secret variable names")
  static final String INLINE_SECRET = """
      package com.acme;
      
      public class Config {
          private static final String API_KEY = "sk-prod-x9f2mZ3bqW9pR2xYz1A2B3C";
      }
      """;

  @ShouldFind(severity = Severity.CRITICAL, minConfidence = 0.85)
  @SpecEvidence("String literal matching common AWS secret access key pattern")
  static final String AWS_SECRET = """
      package com.acme;
      
      public class AwsConfig {
          public AwsConfig() {
              String secret = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("@Value injection is the correct externalisation pattern")
  static final String SPRING_VALUE = """
      package com.acme;
      
      import org.springframework.beans.factory.annotation.Value;
      
      public class Config {
          @Value("${api.key}")
          private String apiKey;
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Log-level test credentials are not hardcoded secrets")
  static final String TEST_CREDENTIAL = """
      package com.acme;
      
      public class Config {
          private String password = "test";
      }
      """;
      
  @ShouldNotFind
  @SpecEvidence("Environment variable references are not secrets")
  static final String ENV_VAR_REF_CREDENTIAL = """
      package com.acme;
      
      public class Config {
          private String apiKey = "${API_KEY}";
      }
      """;
}
