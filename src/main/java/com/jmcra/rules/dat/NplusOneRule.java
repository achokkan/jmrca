package com.jmcra.rules.dat;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
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
 * Rule Evaluator for DAT-001: JPA N+1 lazy fetch check.
 */
@RuleDefinition(id = "DAT-001", domain = Domain.DAT, severity = Severity.HIGH)
@Component
public class NplusOneRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "DAT-001";
  }

  @Override
  public Severity defaultSeverity() {
    return Severity.HIGH;
  }

  @Override
  public Flux<Finding> evaluate(DomainContext ctx) {
    return Flux.fromStream(
        ctx.astIndex().compilationUnits().entrySet().stream()
            .flatMap(entry -> {
              String filePath = entry.getKey();
              CompilationUnit cu = entry.getValue();
              return cu.findAll(FieldDeclaration.class).stream()
                  .filter(f -> f.isAnnotationPresent("OneToMany"))
                  .filter(f -> !f.isAnnotationPresent("BatchSize"))
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("JPA @OneToMany without explicit @BatchSize")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.getVariables().get(0).toString())
                         .message("A `@OneToMany` mapping without an explicit `@BatchSize` is highly susceptible to the N+1 query problem, severely degrading database performance when hydrating collections.")
                         .remediation("Annotate the collection with `@BatchSize(size = 50)` (or appropriately sized) to buffer loads, or switch to explicit JOIN FETCH queries.")
                         .references(List.of("Vlad-Mihalcea-N+1"))
                         .confidence(0.95)
                         .ruleVersion("1.2")
                         .build();
                  });
            })
    );
  }
}
