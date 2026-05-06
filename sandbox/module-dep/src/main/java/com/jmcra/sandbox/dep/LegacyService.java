package com.jmcra.sandbox.dep;

import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.stereotype.Component;

/**
 * Legacy service using outdated injection annotations.
 * Targets: DEP-005 (javax.inject detected).
 */
@Component
@Named("legacyService")
public class LegacyService {

    private final String name;

    // DEP-005: Using javax.inject.Inject instead of jakarta.inject.Inject
    @Inject
    public LegacyService() {
        this.name = "legacy";
    }

    public String getName() {
        return name;
    }
}
