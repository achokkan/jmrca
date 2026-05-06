package com.jmcra.rules.dep;

import com.jmcra.annotations.RuleContractTest;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.jmcra.model.DependencyTree;
import com.jmcra.model.Dependency;
import com.jmcra.pipeline.stage4.DomainContext;
import java.util.List;
import reactor.test.StepVerifier;
import java.nio.file.Path;
import java.util.Map;
import com.jmcra.pipeline.stage4.AstIndex;
import com.jmcra.model.ScanRequest;

/**
 * Tests for DEP-001: CVE dependency
 * SPC-070: DEP-001 positive: CVE dependency
 * SPC-071: DEP-001 negative: clean dependency
 */
@RuleContractTest(specClause = "SPC-070,SPC-071", ruleId = "DEP-001")
class CveDependencyContractTest {

  @Test
  void detectsKnownCveVulnerability() {
     var req = ScanRequest.builder().commitSha("1234567890123456789012345678901234567890").branch("main").repositoryUrl("none").build();
     var deps = List.of(new Dependency("org.apache.logging.log4j", "log4j-core", "2.14.1", "compile", false));
     var depTree = new DependencyTree("maven", deps, deps);
     
     var ctx = new DomainContext(
         req, AstIndex.empty(), depTree, Map.of(), List.of(), Path.of(""), Map.of()
     );
     
     var rule = new CveDependencyRule();
     StepVerifier.create(rule.evaluate(ctx))
         .assertNext(finding -> {
             assertThat(finding.ruleId()).isEqualTo("DEP-001");
             assertThat(finding.title()).contains("CVE");
             assertThat(finding.message()).contains("log4j-core:2.14.1");
         })
         .verifyComplete();
  }

  @Test
  void ignoresCleanDependencies() {
     var req = ScanRequest.builder().commitSha("1234567890123456789012345678901234567890").branch("main").repositoryUrl("none").build();
     var deps = List.of(new Dependency("org.springframework.boot", "spring-boot-starter", "4.0.0", "compile", false));
     var depTree = new DependencyTree("maven", deps, deps);
     
     var ctx = new DomainContext(
         req, AstIndex.empty(), depTree, Map.of(), List.of(), Path.of(""), Map.of()
     );
     
     var rule = new CveDependencyRule();
     StepVerifier.create(rule.evaluate(ctx))
         .verifyComplete();
  }
}
