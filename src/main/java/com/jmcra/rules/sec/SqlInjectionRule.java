package com.jmcra.rules.sec;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
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
 * Rule Evaluator for SEC-002: SQL/JPQL injection via string concatenation in query parameter.
 * <p>
 * Signals: BinaryExpr (+) in JPQL/SQL string building inside createQuery or executeQuery.
 */
@RuleDefinition(id = "SEC-002", domain = Domain.SEC, severity = Severity.HIGH)
@Component
public class SqlInjectionRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "SEC-002";
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
              return detectSqlInjection(filePath, cu).stream();
            })
    );
  }

  private List<Finding> detectSqlInjection(String filePath, CompilationUnit cu) {
    return cu.findAll(MethodCallExpr.class).stream()
        .filter(m -> {
          String name = m.getNameAsString();
          return name.equals("createQuery") || name.equals("createNativeQuery") || name.equals("executeQuery");
        })
        .filter(m -> m.getArguments().size() > 0 && m.getArgument(0).isBinaryExpr())
        .map((MethodCallExpr expr) -> {
          BinaryExpr binaryExpr = expr.getArgument(0).asBinaryExpr();
          boolean isConcat = binaryExpr.getOperator() == BinaryExpr.Operator.PLUS;
          if (isConcat) {
            int line = expr.getBegin().map(p -> p.line).orElse(1);
            int col = expr.getBegin().map(p -> p.column).orElse(1);
            return Finding.builder(ruleId(), defaultSeverity())
                .title("SQL/JPQL injection via string concatenation")
                .file(filePath)
                .line(line)
                .column(col)
                .snippet(expr.toString().replace("\n", ""))
                .message("String concatenation was found within a query creation method call (" + expr.getNameAsString() + "). This leads to SQL Injection.")
                .remediation("Use parameterized queries e.g. `.setParameter(1, value)` instead of concatenation.")
                .references(List.of("CWE-89", "OWASP-A03:2021"))
                .confidence(0.90)
                .ruleVersion("1.1")
                .build();
          }
          return (Finding) null;
        })
        .filter(java.util.Objects::nonNull)
        .collect(java.util.stream.Collectors.toList());
  }
}
