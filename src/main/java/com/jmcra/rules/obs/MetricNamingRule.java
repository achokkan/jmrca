package com.jmcra.rules.obs;

import com.github.javaparser.ast.CompilationUnit;
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
import java.util.regex.Pattern;

/**
 * Rule Evaluator for OBS-003: Prometheus Metric Naming 
 */
@RuleDefinition(id = "OBS-003", domain = Domain.OBS, severity = Severity.MEDIUM)
@Component
public class MetricNamingRule implements RuleEvaluator {

  private static final Pattern SNAKE_CASE = Pattern.compile("^[a-z0-9_]+$");

  @Override
  public String ruleId() {
    return "OBS-003";
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
              return cu.findAll(MethodCallExpr.class).stream()
                  .filter(m -> {
                      String name = m.getNameAsString();
                      return name.equals("counter") || name.equals("timer") || name.equals("gauge") || name.equals("summary");
                  })
                  .filter(m -> m.getArguments().size() > 0 && m.getArgument(0).isStringLiteralExpr())
                  .map(stmt -> {
                      String metricName = stmt.getArgument(0).asStringLiteralExpr().getValue();
                      if (!SNAKE_CASE.matcher(metricName.replace(".", "_")).matches()) {
                          int line = stmt.getBegin().map(p -> p.line).orElse(1);
                          int col = stmt.getBegin().map(p -> p.column).orElse(1);
                          return Finding.builder(ruleId(), defaultSeverity())
                             .title("Custom metric skips Prometheus conventions")
                             .file(filePath)
                             .line(line)
                             .column(col)
                             .snippet(metricName)
                             .message("Metric names should be snake_case (or dot separated) and not camelCase (e.g., 'http_requests_total'). Encountered: " + metricName)
                             .remediation("Rename metric to match snake_case standard.")
                             .references(List.of("Prometheus-Naming-Docs"))
                             .confidence(0.98)
                             .ruleVersion("1.0")
                             .build();
                      }
                      return null;
                  })
                  .filter(java.util.Objects::nonNull);
            })
    );
  }
}
