package com.jmcra.sandbox.dat;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Compliant data access service for JMCRA testing.
 */
@Entity
@Table(name = "products")
class ProductEntity {
    @Id @GeneratedValue
    private Long id;

    private String name;

    /**
     * DAT-001 Compliant: fetch=LAZY and explicit BatchSize to mitigate N+1.
     */
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<ReviewEntity> reviews;
}

@Entity
@Table(name = "reviews")
class ReviewEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}

@Service
public class EfficientService {

    private final EntityManager entityManager;

    public EfficientService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * DAT-002 Compliant: readOnly=true for query-only method.
     */
    @Transactional(readOnly = true)
    public List<ProductEntity> findAllProducts() {
        return entityManager.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class)
            .getResultList();
    }

    /**
     * Normal write transaction — not flagged.
     */
    @Transactional
    public void saveProduct(ProductEntity p) {
        entityManager.persist(p);
    }
}
