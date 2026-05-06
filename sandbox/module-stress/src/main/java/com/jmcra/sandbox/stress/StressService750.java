package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService750 {
    public String performTask750() {
        return "Task 750 result";
    }
    
    public void crossCall(StressService751 other) {
        other.performTask751();
    }
}
