package com.jmcra.sandbox.adversarial;

import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.lang.reflect.Field;

/**
 * Adversarial evasion samples for JMCRA testing.
 * Designed to test the agent's reasoning depth (Section 7.1).
 */
@RestController
@RequestMapping("/api/evasion")
public class EvasionController {

    /**
     * SEC-001 Evasion: Obfuscated secret via Base64 and Reflection.
     * A simple regex will miss this, but a deep semantic review should flag it.
     */
    public void leakSecret() throws Exception {
        String encoded = "c2stcHJvZC14OWYybVo="; // sk-prod-x9f2mZ
        String decoded = new String(Base64.getDecoder().decode(encoded));
        
        // Using reflection to set a sensitive field
        Field field = this.getClass().getDeclaredField("apiKey");
        field.setAccessible(true);
        field.set(this, decoded);
    }

    private String apiKey;

    /**
     * SEC-002 Evasion: Complex SQL concatenation using StringBuilder and 
     * multiple method calls.
     */
    public String buildNastyQuery(String input) {
        StringBuilder sb = new StringBuilder("SELECT * FROM products WHERE ");
        sb.append(getFilter(input));
        return sb.toString();
    }

    private String getFilter(String input) {
        return "name LIKE '%" + input + "%'";
    }

    /**
     * RES-001 Evasion: Hiding a blocking call inside a custom library-like class.
     */
    public void callUnreliableService() {
        LegacyWrapper.execute(() -> {
            // This execute method internally does NOT have a circuit breaker
            // even if the outer class looks like it might.
        });
    }
}

class LegacyWrapper {
    public static void execute(Runnable r) {
        r.run(); // No resilience here
    }
}
