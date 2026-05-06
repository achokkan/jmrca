package com.jmcra.rules.concurrent;

import java.util.List;

import org.springframework.stereotype.Component;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.jmcra.model.Domain;
import com.jmcra.model.Finding;
import com.jmcra.model.Severity;
import com.jmcra.pipeline.stage4.DomainContext;
import com.jmcra.pipeline.stage4.RuleEvaluator;
import com.jmcra.rules.annotations.RuleDefinition;

import reactor.core.publisher.Flux;

/**
 * Rule Evaluator for CON-001: Blocking call on Reactor thread.
 */
@RuleDefinition(id = "CON-001", domain = Domain.CON, severity = Severity.CRITICAL)
@Component
public class ReactorBlockingRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "CON-001";
  }

  @Override
  public Severity defaultSeverity() {
    return Severity.CRITICAL;
  }

  @Override
  public Flux<Finding> evaluate(DomainContext ctx) {
    return Flux.fromStream(
        ctx.astIndex().compilationUnits().entrySet().stream()
            .flatMap(entry -> {
              String filePath = entry.getKey();
              CompilationUnit cu = entry.getValue();
              return cu.findAll(MethodCallExpr.class).stream()
                  .filter(m -> {
                       String name = m.getNameAsString();
                       // Extremely simplified AST heuristic. BlockHound does this at runtime.
                       return (name.equals("sleep") && m.getScope().map(s -> s.toString().equals("Thread")).orElse(false)) ||
                              name.equals("readLine");
                  })
                  .filter(m -> m.findAncestor(MethodCallExpr.class)
                                .map(parent -> parent.getNameAsString().equals("map") || parent.getNameAsString().equals("flatMap"))
                                .orElse(false))
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("Blocking call detected within a Reactor stream")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.toString())
                         .message("Calling generic blocking boundaries (`Thread.sleep`, blocking I/O) within a reactive operator (map/flatMap) blocks the event loop scheduling queue.")
                         .remediation("Use reactive alternatives like `Mono.delay` or offload to a bounded elastic scheduler using `.subscribeOn(Schedulers.boundedElastic())`.")
                         .references(List.of("Reactor-BlockHound"))
                         .confidence(0.95)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
