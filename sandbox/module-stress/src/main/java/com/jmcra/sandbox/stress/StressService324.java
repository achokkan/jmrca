package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService324 {
    public String performTask324() {
        return "Task 324 result";
    }
    
    public void crossCall(StressService325 other) {
        other.performTask325();
    }
}
