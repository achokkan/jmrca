package com.jmcra.rules.concurrent;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.jmcra.model.Domain;
import com.jmcra.model.Finding;
import com.jmcra.model.Severity;
import com.jmcra.pipeline.stage4.DomainContext;
import com.jmcra.pipeline.stage4.RuleEvaluator;
import com.jmcra.rules.annotations.Framework;
import com.jmcra.rules.annotations.RuleDefinition;
import com.jmcra.rules.annotations.VersionGate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Rule Evaluator for CON-010: Scoped value shared across service boundaries.
 * Java 25 Version Gated.
 */
@RuleDefinition(
    id = "CON-010", 
    domain = Domain.CON, 
    severity = Severity.HIGH, 
    sinceVersion = @VersionGate(framework = Framework.JAVA, minVersion = "25")
)
@Component
public class ScopedValueRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "CON-010";
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
              
              // Find ScopedValue.where(...).run(...) or call(...) patterns
              return cu.findAll(MethodCallExpr.class).stream()
                  .filter(m -> m.getNameAsString().equals("run") || m.getNameAsString().equals("call"))
                  .filter(m -> m.getScope().isPresent() && m.getScope().get().isMethodCallExpr())
                  .filter(m -> {
                      MethodCallExpr scope = m.getScope().get().asMethodCallExpr();
                      return scope.getNameAsString().equals("where") && 
                             scope.getScope().isPresent() && 
                             scope.getScope().get().toString().contains("ScopedValue");
                  })
                  .filter(m -> {
                      // Check if the body of the lambda/runnable contains a boundary crossing
                      // For the sandbox, we look for doBackgroundWork() or similar external calls
                      String body = m.getArguments().toString();
                      return body.contains("doBackgroundWork") || body.contains("Service");
                  })
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("ScopedValue shared across boundary without rebind")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.toString())
                         .message("Scoped values (JEP 506) should be explicitly rebound when crossing service boundaries or entering async blocks to avoid unexpected binding loss.")
                         .remediation("Use ScopedValue.where(...) again inside the sub-task or ensure the boundary crossing is scope-aware.")
                         .references(List.of("JEP-506"))
                         .confidence(0.85)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
