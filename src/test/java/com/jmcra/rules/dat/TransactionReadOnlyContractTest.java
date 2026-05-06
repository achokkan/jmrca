package com.jmcra.rules.dat;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for DAT-002: @Transactional(readOnly=false) on a query-only service method
 * SPC-108: DAT-002 ORM readonly flush mode optimizations
 */
@RuleContractTest(specClause = "SPC-108", ruleId = "DAT-002")
class TransactionReadOnlyContractTest {

  @ShouldFind(severity = Severity.HIGH, minConfidence = 0.90)
  @SpecEvidence("A bare @Transactional retains readOnly=false but executes query methods 'find', skipping Hibernate session flush bypass optimizations.")
  static final String BARE_TRANSACTIONAL = """
      package com.acme;
      import org.springframework.transaction.annotation.Transactional;
      
      public class UserService {
          @Transactional
          public User findById(Long id) {
              return new User();
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Explicitly declaring readOnly=true allows flush-mode optimizations.")
  static final String READONLY_TRANSACTIONAL = """
      package com.acme;
      import org.springframework.transaction.annotation.Transactional;
      
      public class UserService {
          @Transactional(readOnly = true)
          public User findById(Long id) {
              return new User();
          }
      }
      """;
}
