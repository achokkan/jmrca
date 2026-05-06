package com.jmcra.rules.api;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.stmt.ThrowStmt;
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
 * Rule Evaluator for API-001: REST endpoint throws unhandled exception
 */
@RuleDefinition(id = "API-001", domain = Domain.API, severity = Severity.HIGH)
@Component
public class Http500HandlerRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "API-001";
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
              return cu.findAll(ClassOrInterfaceDeclaration.class).stream()
                  .filter(c -> c.isAnnotationPresent("RestController") || c.isAnnotationPresent("Controller"))
                  .filter(c -> !c.getMethods().stream().anyMatch(m -> m.isAnnotationPresent("ExceptionHandler"))) // Simple intra-class check. Cross-class '@ControllerAdvice' would require JGraphT symbol resolution.
                  .flatMap(c -> c.findAll(ThrowStmt.class).stream())
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("REST endpoint returns HTTP 500 on known business exception")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.toString())
                         .message("An explicit `throw` statement was found within a `@RestController` that does not declare an intra-class `@ExceptionHandler`. This exposes internal stack traces directly to API clients (RFC-9110 violation).")
                         .remediation("Implement an `@ExceptionHandler` inside the controller or globally via `@ControllerAdvice` mapping the exception to a proper `ProblemDetail` or `ResponseEntity`.")
                         .references(List.of("RFC-9110"))
                         .confidence(0.80)
                         .ruleVersion("1.1")
                         .build();
                  });
            })
    );
  }
}
