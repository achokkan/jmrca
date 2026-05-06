package com.jmcra.sandbox.sec;

import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

/**
 * Deliberately vulnerable controller for JMCRA testing.
 * Targets: SEC-001, SEC-002, SEC-004.
 */
@RestController
@RequestMapping("/api/vulnerable")
public class VulnerableController {

    private final EntityManager entityManager;

    // SEC-001: Hardcoded API Key literal
    private static final String AWS_SECRET_KEY = "AKIAEXAMPLE1234567890/SECRET";

    public VulnerableController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * SEC-002: SQL Injection via string concatenation.
     * SEC-004: Missing @PreAuthorize on a public REST endpoint.
     */
    @GetMapping("/users/search")
    public List<?> searchUsers(@RequestParam String name) {
        // Vulnerable to SQLi
        String sql = "SELECT * FROM users WHERE name = '" + name + "'";
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

    /**
     * SEC-004: Public endpoint with no security annotations.
     */
    @PostMapping("/admin/delete-all")
    public void deleteAllData() {
        // Destructive operation without authorization check
        entityManager.createNativeQuery("DELETE FROM orders").executeUpdate();
    }
}
