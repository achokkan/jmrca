package com.jmcra.rules.obs;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
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
 * Rule Evaluator for OBS-001: Trace propagation
 */
@RuleDefinition(id = "OBS-001", domain = Domain.OBS, severity = Severity.HIGH)
@Component
public class TracePropagationRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "OBS-001";
  }

  @Override
  public Severity defaultSeverity() {
    return Severity.HIGH;
  }

  @Override
  public Flux<Finding> evaluate(DomainContext ctx) {
      if (ctx.dependencyTree().containsGroup("io.opentelemetry.instrumentation")) {
         // Autoinstrumentation passes the trace seamlessly. We skip.
         return Flux.empty();
      }

      return Flux.fromStream(
        ctx.astIndex().compilationUnits().entrySet().stream()
            .flatMap(entry -> {
              String filePath = entry.getKey();
              CompilationUnit cu = entry.getValue();
              return cu.findAll(ClassOrInterfaceDeclaration.class).stream()
                  .filter(c -> c.isAnnotationPresent("FeignClient") 
                          && !cu.findAll(MethodCallExpr.class).stream().anyMatch(m -> m.getNameAsString().equals("header") && m.getArguments().toString().contains("traceparent")))
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("Trace context not propagated across @FeignClient boundary")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.getNameAsString())
                         .message("Observability contexts (W3C traceparent, etc) must be forwarded to ensure traces are not broken.")
                         .remediation("Use `opentelemetry-spring-boot-starter` or add a `RequestInterceptor` forwarding `traceparent`.")
                         .references(List.of("OpenTelemetry-Spec", "W3C-TraceContext"))
                         .confidence(0.95)
                         .ruleVersion("1.2")
                         .build();
                  });
            })
      );
  }
}
