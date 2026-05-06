package com.jmcra.rules.sec;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.jmcra.model.Domain;
import com.jmcra.model.Finding;
import com.jmcra.model.Severity;
import com.jmcra.pipeline.stage4.DomainContext;
import com.jmcra.pipeline.stage4.RuleEvaluator;
import com.jmcra.rules.annotations.RuleDefinition;
import com.jmcra.rules.annotations.VersionGate;
import com.jmcra.rules.annotations.Framework;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Rule Evaluator for SEC-012: PEM-encoded key (JEP 470) loaded from hardcoded file path.
 * Version Gated for Java 25.
 */
@RuleDefinition(id = "SEC-012", domain = Domain.SEC, severity = Severity.HIGH, sinceVersion = @VersionGate(framework = Framework.JAVA, minVersion = "25"))
@Component
public class PemKeyRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "SEC-012";
  }

  @Override
  public Severity defaultSeverity() {
    return Severity.HIGH;
  }

  @Override
  public Flux<Finding> evaluate(DomainContext ctx) {
    if (!ctx.astIndex().isJavaVersionAtLeast(25)) {
      return Flux.empty();
    }
    
    return Flux.fromStream(
        ctx.astIndex().compilationUnits().entrySet().stream()
            .flatMap(entry -> detectHardcodedPems(entry.getKey(), entry.getValue()).stream())
    );
  }

  private List<Finding> detectHardcodedPems(String filePath, CompilationUnit cu) {
    return cu.findAll(StringLiteralExpr.class).stream()
        .filter(s -> {
          String val = s.getValue().toLowerCase();
          return val.endsWith(".pem") || val.endsWith(".key") || val.endsWith(".crt") || val.contains("private_key");
        })
        .filter(s -> s.findAncestor(com.github.javaparser.ast.expr.MethodCallExpr.class).isPresent() ||
                     s.findAncestor(com.github.javaparser.ast.expr.ObjectCreationExpr.class).isPresent())
        .map(expr -> {
            int line = expr.getBegin().map(p -> p.line).orElse(1);
            int col = expr.getBegin().map(p -> p.column).orElse(1);
            return Finding.builder(ruleId(), defaultSeverity())
                .title("Hardcoded PEM-encoded key file path")
                .file(filePath)
                .line(line)
                .column(col)
                .snippet(expr.toString())
                .message("Loading PEM files (JEP 470) from a hardcoded string path exposes environment topology. Files should be injected via configuration.")
                .remediation("Inject the PEM path via `@Value` or environment variables instead of hardcoding the path.")
                .references(List.of("CWE-321", "JEP-470"))
                .confidence(0.90)
                .ruleVersion("1.0")
                .build();
        })
        .toList();
  }
}
