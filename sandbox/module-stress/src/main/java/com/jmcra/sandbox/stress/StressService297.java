package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService297 {
    public String performTask297() {
        return "Task 297 result";
    }
    
    public void crossCall(StressService298 other) {
        other.performTask298();
    }
}
