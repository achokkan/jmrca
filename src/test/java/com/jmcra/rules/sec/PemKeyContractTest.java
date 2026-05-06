package com.jmcra.rules.sec;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Contract Tests for SEC-012: PEM-encoded key (Java 25 JEP 470) loaded from hardcoded path.
 * <p>
 * SPC-050: SEC-012 positive/negative
 */
@RuleContractTest(specClause = "SPC-050", ruleId = "SEC-012")
class PemKeyContractTest {

  @ShouldFind(severity = Severity.HIGH, minConfidence = 0.90)
  @SpecEvidence("Paths to PEMs cannot be hardcoded strings")
  static final String HARDCODED_PEM = """
      package com.acme;
      import java.security.cert.CertificateFactory;
      import java.io.FileInputStream;
      
      public class TlsConfig {
          public void loadPem() throws Exception {
              var pem = new FileInputStream("/etc/certs/private.pem");
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Dynamic loading via environment configuration is safe")
  static final String DYNAMIC_PEM = """
      package com.acme;
      import java.security.cert.CertificateFactory;
      import java.io.FileInputStream;
      
      public class TlsConfig {
          public void loadPem(String certPath) throws Exception {
              var pem = new FileInputStream(certPath);
          }
      }
      """;
}
