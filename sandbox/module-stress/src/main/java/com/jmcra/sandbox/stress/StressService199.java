package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService199 {
    public String performTask199() {
        return "Task 199 result";
    }
    
    public void crossCall(StressService200 other) {
        other.performTask200();
    }
}
