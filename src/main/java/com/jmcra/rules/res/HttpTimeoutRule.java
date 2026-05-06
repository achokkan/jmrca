package com.jmcra.rules.res;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
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
 * Rule Evaluator for RES-002: HTTP client without explicit timeout.
 */
@RuleDefinition(id = "RES-002", domain = Domain.RES, severity = Severity.HIGH)
@Component
public class HttpTimeoutRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "RES-002";
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
              return cu.findAll(ObjectCreationExpr.class).stream()
                  .filter(o -> o.getType().getNameAsString().equals("RestTemplate") ||
                               o.getType().getNameAsString().equals("OkHttpClient"))
                  // if they just do `new RestTemplate()` without builder injection
                  .filter(o -> o.getArguments().size() == 0) 
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("HTTP client initialized without explicit timeouts")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.toString())
                         .message("Default `RestTemplate` and `OkHttpClient` typically do not have timeouts, leading to thread exhaustion if the target service hangs.")
                         .remediation("Use `RestTemplateBuilder` to inject explicit `setConnectTimeout` and `setReadTimeout`.")
                         .references(List.of("Hystrix-BP"))
                         .confidence(0.85)
                         .ruleVersion("1.1")
                         .build();
                  });
            })
    );
  }
}
