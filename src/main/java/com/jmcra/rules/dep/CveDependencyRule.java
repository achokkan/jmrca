package com.jmcra.rules.dep;

import com.jmcra.model.Domain;
import com.jmcra.model.Finding;
import com.jmcra.model.Severity;
import com.jmcra.pipeline.stage4.DomainContext;
import com.jmcra.pipeline.stage4.RuleEvaluator;
import com.jmcra.rules.annotations.RuleDefinition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Rule Evaluator for DEP-001: Dependency with known CVE (CVSS >= 9.0)
 * Note: A real implementation would query OSS-Index or similar, but for
 * this Phase we assert statically known critical offenders like log4j 2.14.x.
 */
@RuleDefinition(id = "DEP-001", domain = Domain.DEP, severity = Severity.CRITICAL)
@Component
public class CveDependencyRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "DEP-001";
  }

  @Override
  public Severity defaultSeverity() {
    return Severity.CRITICAL;
  }

  @Override
  public Flux<Finding> evaluate(DomainContext ctx) {
    return Flux.fromIterable(ctx.dependencyTree().allDependencies())
        .filter(dep -> 
            (dep.groupId().equals("org.apache.logging.log4j") && dep.artifactId().equals("log4j-core") && dep.version().startsWith("2.14")) ||
            (dep.groupId().equals("commons-collections") && dep.artifactId().equals("commons-collections") && dep.version().equals("3.2.1"))
        )
        .map(dep -> Finding.builder(ruleId(), defaultSeverity())
            .title("Dependency with known CVE (CVSS >= 9.0) detected")
            .file(ctx.dependencyTree().buildTool().equals("maven") ? "pom.xml" : "build.gradle")
            .line(1)
            .column(0)
            .snippet(dep.groupId() + ":" + dep.artifactId() + ":" + dep.version())
            .message("A dependency with a CRITICAL CVE known vulnerability was found: " + dep.groupId() + ":" + dep.artifactId() + ":" + dep.version())
            .remediation("Update dependency version dynamically using Dependabot or manually bump.")
            .references(List.of("NVD", "OSS-Index"))
            .confidence(1.0)
            .ruleVersion("1.2")
            .build()
        );
  }
}
