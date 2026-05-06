package com.jmcra.rules.dep;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.jmcra.model.Domain;
import com.jmcra.model.Finding;
import com.jmcra.model.Severity;
import com.jmcra.pipeline.stage4.DomainContext;
import com.jmcra.pipeline.stage4.RuleEvaluator;
import com.jmcra.rules.annotations.RuleDefinition;
import com.jmcra.rules.annotations.VersionGate;
import com.jmcra.rules.annotations.Framework;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Rule Evaluator for DEP-005: legacy javax.inject imports.
 */
@RuleDefinition(id = "DEP-005", domain = Domain.DEP, severity = Severity.MEDIUM, sinceVersion = @VersionGate(framework = Framework.SPRING_CLOUD, minVersion = "2025.1.0"))
@Component
public class JavaxInjectRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "DEP-005";
  }

  @Override
  public Severity defaultSeverity() {
    return Severity.MEDIUM;
  }

  @Override
  public Flux<Finding> evaluate(DomainContext ctx) {
    // Only applies if Spring Cloud Oakwood is present or target
    // The AstIndex version check could be applied here if needed.
    return Flux.fromStream(
        ctx.astIndex().compilationUnits().entrySet().stream()
            .flatMap(entry -> {
              String filePath = entry.getKey();
              CompilationUnit cu = entry.getValue();
              return cu.findAll(ImportDeclaration.class).stream()
                  .filter(i -> i.getNameAsString().startsWith("javax.inject"))
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("Legacy javax.inject import detected")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.toString().replace("\n", ""))
                         .message("The `javax.inject` package is incompatible with Spring Cloud Oakwood (2025.1.x) or Spring Boot 3+ / 4.")
                         .remediation("Migrate the import to `jakarta.inject`.")
                         .references(List.of("Spring-Cloud-2025.1-Migration"))
                         .confidence(1.0)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
