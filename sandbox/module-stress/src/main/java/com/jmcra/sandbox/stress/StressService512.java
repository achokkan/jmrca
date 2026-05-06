package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService512 {
    public String performTask512() {
        return "Task 512 result";
    }
    
    public void crossCall(StressService513 other) {
        other.performTask513();
    }
}
