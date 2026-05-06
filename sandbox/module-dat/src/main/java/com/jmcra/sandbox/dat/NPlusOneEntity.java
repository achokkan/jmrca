package com.jmcra.sandbox.dat;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Data access fixtures for JMCRA testing.
 * Targets: DAT-001, DAT-002.
 */
@Entity
@Table(name = "customers")
public class NPlusOneEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    /**
     * DAT-001: N+1 query risk.
     * fetch=FetchType.EAGER is used, and no @BatchSize is specified.
     */
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderEntity> orders;

    // Getters/Setters
}

@Entity
@Table(name = "orders")
class OrderEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private NPlusOneEntity customer;
}

@Service
class HeavyService {

    private final EntityManager entityManager;

    public HeavyService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * DAT-002: @Transactional(readOnly=false) on a query-only method.
     * This is the default, but should be flagged if no mutations occur.
     */
    @Transactional
    public List<NPlusOneEntity> findAllCustomers() {
        return entityManager.createQuery("SELECT c FROM NPlusOneEntity c", NPlusOneEntity.class)
            .getResultList();
    }
}
