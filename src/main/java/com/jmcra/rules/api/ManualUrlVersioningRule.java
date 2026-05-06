package com.jmcra.rules.api;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
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
 * Rule Evaluator for API-005: Manual URL-prefix versioning (/v1/, /v2/)
 * Spring Boot 4 Native API versioning framework detector.
 */
@RuleDefinition(id = "API-005", domain = Domain.API, severity = Severity.LOW)
@Component
public class ManualUrlVersioningRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "API-005";
  }

  @Override
  public Severity defaultSeverity() {
    return Severity.LOW;
  }

  @Override
  public Flux<Finding> evaluate(DomainContext ctx) {
    return Flux.fromStream(
        ctx.astIndex().compilationUnits().entrySet().stream()
            .flatMap(entry -> {
              String filePath = entry.getKey();
              CompilationUnit cu = entry.getValue();
              return cu.findAll(com.github.javaparser.ast.expr.AnnotationExpr.class).stream()
                  .filter(a -> a.getNameAsString().equals("RequestMapping") || 
                               a.getNameAsString().equals("GetMapping") || 
                               a.getNameAsString().equals("PostMapping"))
                  .filter(a -> {
                      if (a.isSingleMemberAnnotationExpr()) {
                          String path = a.asSingleMemberAnnotationExpr().getMemberValue().toString();
                          return path.contains("/v1/") || path.contains("/v2/") || path.contains("/v3/");
                      } else if (a.isNormalAnnotationExpr()) {
                          return a.asNormalAnnotationExpr().getPairs().stream()
                                  .filter(p -> p.getNameAsString().equals("value") || p.getNameAsString().equals("path"))
                                  .anyMatch(p -> {
                                      String path = p.getValue().toString();
                                      return path.contains("/v1/") || path.contains("/v2/") || path.contains("/v3/");
                                  });
                      }
                      return false;
                  })
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("Manual URL-prefix versioning detected")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.toString())
                         .message("Starting with Spring Boot 4, manual URL prefix version mappings (`/v1/users`) are an anti-pattern compared to native `@ApiVersion` router boundaries.")
                         .remediation("Drop the prefix and annotate your controller with Spring Boot 4's `@ApiVersion(\"1\")`.")
                         .references(List.of("Spring-Boot-4-API-Versioning"))
                         .confidence(0.95)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
