package com.jmcra.rules.concurrent;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.TryStmt;
import com.jmcra.model.Domain;
import com.jmcra.model.Finding;
import com.jmcra.model.Severity;
import com.jmcra.pipeline.stage4.DomainContext;
import com.jmcra.pipeline.stage4.RuleEvaluator;
import com.jmcra.rules.annotations.Framework;
import com.jmcra.rules.annotations.RuleDefinition;
import com.jmcra.rules.annotations.VersionGate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Rule Evaluator for CON-011: Structured concurrency task scope not closed.
 * Java 25 Version Gated.
 */
@RuleDefinition(id = "CON-011", domain = Domain.CON, severity = Severity.MEDIUM, sinceVersion = @VersionGate(framework = Framework.JAVA, minVersion = "25"))
@Component
public class StructuredConcurrencyRule implements RuleEvaluator {

  private static final Logger log = LoggerFactory.getLogger(StructuredConcurrencyRule.class);

  @jakarta.annotation.PostConstruct
  public void init() {
    log.info("StructuredConcurrencyRule (CON-011) bean initialized.");
  }

  @Override
  public String ruleId() {
    return "CON-011";
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
              return cu.findAll(ObjectCreationExpr.class).stream()
                  .filter(o -> o.getType().asString().contains("StructuredTaskScope"))
                  .filter(o -> {
                      var tryStmt = o.findAncestor(TryStmt.class);
                      if (tryStmt.isEmpty()) return true;
                      // Check if the variable is declared in the try-with-resources header
                      return tryStmt.get().getResources().stream()
                          .noneMatch(r -> r.toString().contains(o.toString()));
                  })
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("StructuredTaskScope not closed in try-with-resources")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.toString())
                         .message("Structured concurrency scopes (JEP 505) must be declared within a `try-with-resources` block to guarantee thread cancellation and resource cleanup.")
                         .remediation("Wrap the `StructuredTaskScope` instantiation in a `try (var scope = ...)` block.")
                         .references(List.of("JEP-505"))
                         .confidence(0.95)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
