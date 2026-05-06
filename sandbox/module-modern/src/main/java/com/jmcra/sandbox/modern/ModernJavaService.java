package com.jmcra.sandbox.modern;

import org.springframework.web.bind.annotation.*;
import java.util.concurrent.StructuredTaskScope;
import java.util.ScopedValue;

/**
 * Modern Java 25 and Spring Boot 4 fixtures.
 * Targets: CON-010, CON-011, API-005.
 */
@RestController
@RequestMapping("/api/modern")
public class ModernJavaService {

    private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();

    /**
     * CON-010: Scoped value shared across service boundaries without explicit rebind.
     */
    public void processOrder() {
        ScopedValue.where(USER_ID, "user-123").run(() -> {
            // Crossing a boundary (e.g. calling an external service)
            // without explicitly rebinding the scope in the sub-task.
            System.out.println("User: " + USER_ID.get());
            doBackgroundWork();
        });
    }

    private void doBackgroundWork() {
        // Potential leakage or binding loss
    }

    /**
     * CON-011: Structured concurrency task scope not closed in try-with-resources.
     */
    public void concurrentTask() {
        var scope = new StructuredTaskScope.ShutdownOnFailure(); // Missing try-with-resources
        scope.fork(() -> "task1");
        // ...
    }

    /**
     * API-005: Manual URL-prefix versioning detected when Spring Boot 4 native 
     * API versioning is available.
     */
    @GetMapping("/v1/items")
    public String getItemsV1() {
        return "v1 items";
    }

    /**
     * API-005 Compliant: Using Spring Boot 4 native versioning.
     */
    @GetMapping(value = "/items", params = "version=2")
    public String getItemsV2() {
        return "v2 items";
    }
}
