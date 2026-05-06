package com.jmcra.rules.sec;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Contract Tests for SEC-002: SQL/JPQL injection via string concatenation.
 * <p>
 * SPC-044: SEC-002 positive: SQL injection via concatenation
 * SPC-045: SEC-002 negative: parameterised query
 */
@RuleContractTest(specClause = "SPC-044,SPC-045", ruleId = "SEC-002")
class SqlInjectionContractTest {

  @ShouldFind(severity = Severity.HIGH, minConfidence = 0.90)
  @SpecEvidence("JPQL createQuery with string concatenation parameter")
  static final String CONCAT_QUERY = """
      package com.acme;
      import jakarta.persistence.EntityManager;
      
      public class UserRepository {
          private EntityManager em;
          
          public Object findByUsername(String username) {
              return em.createQuery("SELECT u FROM User u WHERE u.username = '" + username + "'").getResultList();
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Use of ? positional parameters is safe")
  static final String PARAMETERISED_QUERY = """
      package com.acme;
      import jakarta.persistence.EntityManager;
      
      public class UserRepository {
          private EntityManager em;
          
          public Object findByUsername(String username) {
              return em.createQuery("SELECT u FROM User u WHERE u.username = ?1")
                       .setParameter(1, username)
                       .getResultList();
          }
      }
      """;
}
