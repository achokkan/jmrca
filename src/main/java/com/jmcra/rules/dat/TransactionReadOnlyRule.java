package com.jmcra.rules.dat;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
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
 * Rule Evaluator for DAT-002: Database Transaction readonly modes.
 */
@RuleDefinition(id = "DAT-002", domain = Domain.DAT, severity = Severity.HIGH)
@Component
public class TransactionReadOnlyRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "DAT-002";
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
              return cu.findAll(MethodDeclaration.class).stream()
                  .filter(m -> m.getNameAsString().startsWith("find") || m.getNameAsString().startsWith("get") || m.getNameAsString().startsWith("read"))
                  .filter(m -> m.isAnnotationPresent("Transactional"))
                  .filter(m -> {
                      var ann = m.getAnnotationByName("Transactional").get();
                      if (ann.isNormalAnnotationExpr()) {
                           return ann.asNormalAnnotationExpr().getPairs().stream()
                                     .noneMatch(p -> p.getNameAsString().equals("readOnly") && p.getValue().toString().equals("true"));
                      }
                      return true;
                  })
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("@Transactional without readOnly=true on query-only method")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.getDeclarationAsString(false, false, true))
                         .message("A method prefixed with `find/get/read` maps strongly to a query-only intent, but is annotated with `@Transactional` missing `readOnly = true`. This forces Hibernate to track dirty states unnecessarily, impacting performance.")
                         .remediation("Update the annotation to `@Transactional(readOnly = true)`.")
                         .references(List.of("Spring-Data-Docs"))
                         .confidence(0.85)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
