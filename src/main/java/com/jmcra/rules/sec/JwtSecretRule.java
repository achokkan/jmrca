package com.jmcra.rules.sec;

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
import java.util.Optional;

/**
 * Rule Evaluator for SEC-003: JWT missing expiry
 * <p>
 * Signals: Jwts.builder() without .expiration() or .setExpiration()
 */
@RuleDefinition(id = "SEC-003", domain = Domain.SEC, severity = Severity.HIGH)
@Component
public class JwtSecretRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "SEC-003";
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
              return detectJwtWeakness(filePath, cu).stream();
            })
    );
  }

  private List<Finding> detectJwtWeakness(String filePath, CompilationUnit cu) {
    return cu.findAll(MethodCallExpr.class).stream()
        .filter(m -> m.getNameAsString().equals("builder") && 
                m.getScope().map(s -> s.toString().equals("Jwts")).orElse(false))
        .map((MethodCallExpr builderInit) -> {
           // Travese up to check for compact() call which tells us the builder is finished
           Optional<MethodCallExpr> compactCall = builderInit.findAncestor(MethodCallExpr.class)
                   .filter(m -> m.getNameAsString().equals("compact"));
                   
           if(compactCall.isPresent()) {
               MethodCallExpr current = compactCall.get();
               boolean hasExpiry = false;
               while(current != null && current.getScope().isPresent() && current.getScope().get().isMethodCallExpr()) {
                   String name = current.getNameAsString();
                   if (name.equals("expiration") || name.equals("setExpiration")) {
                       hasExpiry = true;
                       break;
                   }
                   current = current.getScope().get().asMethodCallExpr();
               }
               // Also check the root one just in case
               if(current != null && (current.getNameAsString().equals("expiration") || current.getNameAsString().equals("setExpiration"))) {
                   hasExpiry = true;
               }
               
               if (!hasExpiry) {
                   int line = builderInit.getBegin().map(p -> p.line).orElse(1);
                   int col = builderInit.getBegin().map(p -> p.column).orElse(1);
                   return Finding.builder(ruleId(), defaultSeverity())
                        .title("JWT missing expiration claim")
                        .file(filePath)
                        .line(line)
                        .column(col)
                        .snippet(compactCall.get().toString().replace("\n", "").replace("\r", "").substring(0, Math.min(compactCall.get().toString().length(), 200)))
                        .message("JWT token is being generated without an expiration claim. This tokens will be valid indefinitely.")
                        .remediation("Chain `.setExpiration()` or `.expiration()` when building the JWT.")
                        .references(List.of("CWE-347", "RFC-7519"))
                        .confidence(0.90)
                        .ruleVersion("1.0")
                        .build();
               }
           }
           return (Finding) null;
        })
        .filter(java.util.Objects::nonNull)
        .collect(java.util.stream.Collectors.toList());
  }
}
