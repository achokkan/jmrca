package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService75 {
    public String performTask75() {
        return "Task 75 result";
    }
    
    public void crossCall(StressService76 other) {
        other.performTask76();
    }
}
