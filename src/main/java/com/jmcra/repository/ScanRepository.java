package com.jmcra.repository;

import com.jmcra.entity.ScanEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * Reactive repository for {@link ScanEntity}.
 */
@Repository
public interface ScanRepository extends ReactiveCrudRepository<ScanEntity, UUID> {

    /**
     * Returns recent scans ordered by start time.
     */
    Flux<ScanEntity> findAllByOrderByStartedAtDesc();
}
