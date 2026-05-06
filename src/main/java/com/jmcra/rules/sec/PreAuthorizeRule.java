package com.jmcra.rules.sec;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
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
 * Rule Evaluator for SEC-004: Missing @PreAuthorize on public @RestController endpoints
 * <p>
 * Signals: @RestController endpoint methods mapped by Spring without explicit authorization checks.
 */
@RuleDefinition(id = "SEC-004", domain = Domain.SEC, severity = Severity.MEDIUM)
@Component
public class PreAuthorizeRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "SEC-004";
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
              return detectMissingAuthorization(filePath, cu).stream();
            })
    );
  }

  private List<Finding> detectMissingAuthorization(String filePath, CompilationUnit cu) {
    return cu.findAll(ClassOrInterfaceDeclaration.class).stream()
        .filter(c -> c.isAnnotationPresent("RestController") || c.isAnnotationPresent("Controller"))
        // we check class level @PreAuthorize if it exists, if so everything is covered.
        .filter(c -> !c.isAnnotationPresent("PreAuthorize") && !c.isAnnotationPresent("Secured") && !c.isAnnotationPresent("RolesAllowed"))
        .flatMap(c -> c.getMethods().stream())
        .filter(MethodDeclaration::isPublic)
        // Check if mapped
        .filter(m -> 
            m.isAnnotationPresent("RequestMapping") ||
            m.isAnnotationPresent("GetMapping") ||
            m.isAnnotationPresent("PostMapping") ||
            m.isAnnotationPresent("PutMapping") ||
            m.isAnnotationPresent("DeleteMapping") ||
            m.isAnnotationPresent("PatchMapping")
        )
        // Check missing auth 
        .filter(m -> 
            !m.isAnnotationPresent("PreAuthorize") && 
            !m.isAnnotationPresent("Secured") &&
            !m.isAnnotationPresent("RolesAllowed")
        )
        .map(m -> {
             int line = m.getBegin().map(p -> p.line).orElse(1);
             int col = m.getBegin().map(p -> p.column).orElse(1);
             return Finding.builder(ruleId(), defaultSeverity())
                  .title("Missing method level method security on Rest Controller")
                  .file(filePath)
                  .line(line)
                  .column(col)
                  .snippet(m.getDeclarationAsString(false, false, true))
                  .message("Public endpoints in @RestController must explicitly declare method-level security if class-level is missing.")
                  .remediation("Add @PreAuthorize(\"hasRole('...')\") or @Secured to define access control restrictions.")
                  .references(List.of("OWASP-A01:2021"))
                  .confidence(0.95)
                  .ruleVersion("1.0")
                  .build();
        })
        .toList();
  }
}
