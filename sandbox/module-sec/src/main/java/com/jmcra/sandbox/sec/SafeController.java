package com.jmcra.sandbox.sec;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Compliant controller for JMCRA testing.
 * JMCRA MUST NOT flag any findings here (False Positive test).
 */
@RestController
@RequestMapping("/api/safe")
public class SafeController {

    private final EntityManager entityManager;

    // SEC-001 Compliant: Secret is externalised via @Value
    @Value("${app.aws.secret-key}")
    private String awsSecretKey;

    public SafeController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * SEC-002 Compliant: Parameterised JPQL query.
     * SEC-004 Compliant: @PreAuthorize is present.
     */
    @GetMapping("/users/search")
    @PreAuthorize("hasRole('USER')")
    public List<?> searchUsers(@RequestParam String name) {
        // Safe from SQLi
        TypedQuery<Object> query = entityManager.createQuery(
            "SELECT u FROM User u WHERE u.name = :name", Object.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    /**
     * SEC-004 Compliant: Admin-only endpoint with explicit auth.
     */
    @PostMapping("/admin/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public void cleanup() {
        entityManager.createQuery("DELETE FROM LogEntity").executeUpdate();
    }
}
