package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService398 {
    public String performTask398() {
        return "Task 398 result";
    }
    
    public void crossCall(StressService399 other) {
        other.performTask399();
    }
}
