package com.jmcra.rules.res;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
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
 * Rule Evaluator for RES-001: Missing CircuitBreaker on FeignClient.
 */
@RuleDefinition(id = "RES-001", domain = Domain.RES, severity = Severity.HIGH)
@Component
public class CircuitBreakerRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "RES-001";
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
                  .filter(c -> c.isAnnotationPresent("FeignClient"))
                  .filter(c -> {
                      // Check for fallback or fallbackFactory
                      boolean hasFallback = c.getAnnotationByName("FeignClient").flatMap(a -> {
                          if (a.isNormalAnnotationExpr()) {
                              NormalAnnotationExpr ne = a.asNormalAnnotationExpr();
                              for (MemberValuePair mvp : ne.getPairs()) {
                                  if (mvp.getNameAsString().equals("fallback") || mvp.getNameAsString().equals("fallbackFactory")) {
                                      return java.util.Optional.of(true);
                                  }
                              }
                          }
                          return java.util.Optional.empty();
                      }).isPresent();
                      
                      boolean hasCircuitBreaker = c.isAnnotationPresent("CircuitBreaker");
                      return !hasFallback && !hasCircuitBreaker;
                  })
                  .map(c -> {
                      int line = c.getBegin().map(p -> p.line).orElse(1);
                      int col = c.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("@FeignClient missing @CircuitBreaker or fallback")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(c.getNameAsString())
                         .message("Remote RPC clients like Feign must be wrapped in CircuitBreakers to prevent cascading failures.")
                         .remediation("Add `fallbackFactory = ...` properties to `@FeignClient` or annotate the interface with `@CircuitBreaker`.")
                         .references(List.of("Netflix-OSS-BP", "Resilience4j-Docs"))
                         .confidence(0.95)
                         .ruleVersion("1.2")
                         .build();
                  });
            })
    );
  }
}
