package com.jmcra.rules.sec;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Contract Tests for SEC-003: JWT secret below 256-bit or HS256 without expiry claim.
 * <p>
 * SPC-046: SEC-003 positive: weak JWT secret
 * SPC-047: SEC-003 negative: strong JWT secret with expiry
 */
@RuleContractTest(specClause = "SPC-046,SPC-047", ruleId = "SEC-003")
class JwtSecretContractTest {

  @ShouldFind(severity = Severity.HIGH, minConfidence = 0.90)
  @SpecEvidence("Building JWT without expiration claim")
  static final String MISSING_EXPIRY = """
      package com.acme;
      import io.jsonwebtoken.Jwts;
      import io.jsonwebtoken.security.Keys;
      
      public class JwtProvider {
          public String createToken() {
              return Jwts.builder()
                  .setSubject("user")
                  .signWith(Keys.hmacShaKeyFor("a_very_long_secret_key_which_is_over_256_bits_for_sure_12345".getBytes()))
                  .compact();
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Building JWT with expiration claim")
  static final String VALID_EXPIRY = """
      package com.acme;
      import io.jsonwebtoken.Jwts;
      import io.jsonwebtoken.security.Keys;
      import java.util.Date;
      
      public class JwtProvider {
          public String createToken() {
              return Jwts.builder()
                  .setSubject("user")
                  .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                  .signWith(Keys.hmacShaKeyFor("a_very_long_secret_key_which_is_over_256_bits_for_sure_12345".getBytes()))
                  .compact();
          }
      }
      """;
}
