package com.jmcra.rules.des;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for DES-001: God class / SRP violation
 * SPC-109: DES-001 Class boundaries
 */
@RuleContractTest(specClause = "SPC-109", ruleId = "DES-001")
class GodClassContractTest {

  @ShouldFind(severity = Severity.MEDIUM, minConfidence = 0.95)
  @SpecEvidence("A class with more than 10 public methods violates the structural Single Responsibility constraints.")
  static final String GOD_CLASS = """
      package com.acme;
      
      public class GodService {
          public void act1() {}
          public void act2() {}
          public void act3() {}
          public void act4() {}
          public void act5() {}
          public void act6() {}
          public void act7() {}
          public void act8() {}
          public void act9() {}
          public void act10() {}
          public void act11() {}
          public void act12() {}
      }
      """;

  @ShouldNotFind
  @SpecEvidence("A class within limits is fine.")
  static final String FINE_CLASS = """
      package com.acme;
      
      public class FineService {
          public void act1() {}
          public void act2() {}
      }
      """;
}
