package com.jmcra.rules.sec;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.jmcra.model.Domain;
import com.jmcra.model.Finding;
import com.jmcra.model.Severity;
import com.jmcra.pipeline.stage4.DomainContext;
import com.jmcra.pipeline.stage4.RuleEvaluator;
import com.jmcra.rules.annotations.RuleDefinition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Locale;

/**
 * Rule Evaluator for SEC-001: Hardcoded credential or API key literal in source.
 * <p>
 * Signals: String literals with high entropy or base64 patterns assigned to common
 * secret variable names ('password', 'apikey', 'secret', etc.).
 */
@RuleDefinition(id = "SEC-001", domain = Domain.SEC, severity = Severity.CRITICAL)
@Component
public class HardcodedCredentialRule implements RuleEvaluator {

  private static final List<String> SECRET_NAMES = List.of(
      "password", "apikey", "secret", "token", "credential"
  );
  
  private static final List<String> SAFE_VALUES = List.of(
      "test", "mock", "placeholder"
  );

  @Override
  public String ruleId() {
    return "SEC-001";
  }

  @Override
  public Severity defaultSeverity() {
    return Severity.CRITICAL;
  }

  @Override
  public Flux<Finding> evaluate(DomainContext ctx) {
    return Flux.fromStream(
        ctx.astIndex().compilationUnits().entrySet().stream()
            .flatMap(entry -> {
              String filePath = entry.getKey();
              CompilationUnit cu = entry.getValue();
              return detectCredentials(filePath, cu).stream();
            })
    );
  }

  private List<Finding> detectCredentials(String filePath, CompilationUnit cu) {
    return cu.findAll(StringLiteralExpr.class).stream()
        .filter(this::isPotentiallySecret)
        .map((StringLiteralExpr expr) -> {
          int line = expr.getBegin().map(p -> p.line).orElse(-1);
          int col = expr.getBegin().map(p -> p.column).orElse(-1);
          return Finding.builder(ruleId(), defaultSeverity())
              .title("Hardcoded credential or API key literal in source")
              .file(filePath)
              .line(line > 0 ? line : 1)
              .column(col > 0 ? col : 1)
              .snippet(expr.toString().replace("\n", "").substring(0, Math.min(expr.toString().length(), 200)))
              .message("A credential literal was detected in source code. This exposes secrets in version control.")
              .remediation("Externalise this configuration using environment variables, `@Value` properties, or a secrets manager like HashiCorp Vault / AWS Secrets Manager.")
              .references(List.of("CWE-798", "OWASP-A07:2021"))
              .confidence(0.95)
              .ruleVersion("1.3")
              .build();
        })
        .collect(java.util.stream.Collectors.toList());
  }

  private boolean isPotentiallySecret(StringLiteralExpr expr) {
    String value = expr.getValue();
    
    // Ignore safe place holders
    if (value.startsWith("${") && value.endsWith("}")) return false;
    if (SAFE_VALUES.contains(value.toLowerCase(Locale.ROOT))) return false;
    
    // Check if the variable name indicates a secret
    boolean isSecretVarName = expr.findAncestor(VariableDeclarationExpr.class).map(vd -> {
      String varName = vd.getVariables().get(0).getNameAsString().toLowerCase(Locale.ROOT);
      return SECRET_NAMES.stream().anyMatch(varName::contains);
    }).orElse(false);

    // If there is no indication of it being a secret variable, base it off entropy
    if (!isSecretVarName) {
         // Some specific patterns, e.g. AWS Keys start with AKIA or might look very random.
         // A very basic entropy check / length check
         if (value.length() >= 16 && hasHighEntropy(value)) {
             return true; 
         }
         return false;
    }

    if (value.length() < 8) return false;
    return true; // We matched a secret variable name with length > 8
  }
  
  private boolean hasHighEntropy(String str) {
      if (str.length() < 10) return false;
      int distinctChars = (int) str.chars().distinct().count();
      return distinctChars > str.length() / 2;
  }
}
