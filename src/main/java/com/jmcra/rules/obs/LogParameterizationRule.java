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
import java.util.Set;

/**
 * Rule Evaluator for OBS-002: Logger concatenation.
 */
@RuleDefinition(id = "OBS-002", domain = Domain.OBS, severity = Severity.MEDIUM)
@Component
public class LogParameterizationRule implements RuleEvaluator {

  private static final Set<String> LOG_METHODS = Set.of(
      "trace", "debug", "info", "warn", "error"
  );

  @Override
  public String ruleId() {
    return "OBS-002";
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
                  .filter(m -> LOG_METHODS.contains(m.getNameAsString()))
                  // simplistic heuristics checks for 'log.' or 'logger.' instance calls
                  .filter(m -> m.getScope().map(s -> s.toString().toLowerCase().contains("log")).orElse(false))
                  .filter(m -> m.getArguments().size() > 0 && m.getArgument(0).isBinaryExpr())
                  .filter(m -> m.getArgument(0).asBinaryExpr().getOperator() == com.github.javaparser.ast.expr.BinaryExpr.Operator.PLUS)
                  .map(m -> {
                      int line = m.getBegin().map(p -> p.line).orElse(1);
                      int col = m.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("Log statement uses string concatenation")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(m.toString().replace("\n", ""))
                         .message("Log framework statements shouldn't concatenate strings. Parameterization defers execution cost only if the log level is actionable.")
                         .remediation("Use SLF4J parameterized logging e.g. `log.info(\"User: {}\", user);`")
                         .references(List.of("SLF4J-Best-Practice"))
                         .confidence(0.85)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
