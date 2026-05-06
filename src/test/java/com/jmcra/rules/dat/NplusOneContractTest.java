package com.jmcra.rules.dat;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for DAT-001: JPA @OneToMany without fetch=LAZY and explicit @BatchSize
 * SPC-089: DAT-001 N+1 lazy fetch
 */
@RuleContractTest(specClause = "SPC-089", ruleId = "DAT-001")
class NplusOneContractTest {

  @ShouldFind(severity = Severity.HIGH, minConfidence = 0.90)
  @SpecEvidence("A bare @OneToMany collection leads to an N+1 query problem without BatchSize.")
  static final String BARE_ONE_TO_MANY = """
      package com.acme;
      import jakarta.persistence.Entity;
      import jakarta.persistence.OneToMany;
      import java.util.List;
      
      @Entity
      public class Company {
          @OneToMany
          private List<Employee> employees;
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Configuring @BatchSize mitigates the N+1 problem gracefully.")
  static final String WITH_BATCH_SIZE = """
      package com.acme;
      import jakarta.persistence.Entity;
      import jakarta.persistence.OneToMany;
      import org.hibernate.annotations.BatchSize;
      import java.util.List;
      
      @Entity
      public class Company {
          @OneToMany
          @BatchSize(size = 50)
          private List<Employee> employees;
      }
      """;
}
