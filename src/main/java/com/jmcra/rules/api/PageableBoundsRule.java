package com.jmcra.rules.api;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.Parameter;
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
 * Rule Evaluator for API-002: Pageable unbounded query sizes.
 */
@RuleDefinition(id = "API-002", domain = Domain.API, severity = Severity.MEDIUM)
@Component
public class PageableBoundsRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "API-002";
  }

  @Override
  public Severity defaultSeverity() {
    return Severity.MEDIUM;
  }

  @Override
  public Flux<Finding> evaluate(DomainContext ctx) {
    return Flux.fromStream(
        ctx.astIndex().compilationUnits().entrySet().stream()
            .flatMap(entry -> {
              String filePath = entry.getKey();
              CompilationUnit cu = entry.getValue();
              return cu.findAll(Parameter.class).stream()
                  .filter(p -> p.getType().asString().equals("Pageable"))
                  .filter(p -> !p.isAnnotationPresent("PageableDefault"))
                  .map(stmt -> {
                      int line = stmt.getBegin().map(pos -> pos.line).orElse(1);
                      int col = stmt.getBegin().map(pos -> pos.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("Pageable endpoint missing max-size guard")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.toString())
                         .message("Passing a RAW `Pageable` argument allows API consumers to request Integer.MAX_VALUE size datasets, exposing the backing database to Memory Exhaustion (OOM) and DoS attacks.")
                         .remediation("Add a `@PageableDefault(size = 50, sort = \"id\")` annotation to bind strict maximum sizes.")
                         .references(List.of("OWASP-API4"))
                         .confidence(0.95)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
