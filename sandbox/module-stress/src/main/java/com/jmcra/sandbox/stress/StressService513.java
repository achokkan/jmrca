package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService513 {
    public String performTask513() {
        return "Task 513 result";
    }
    
    public void crossCall(StressService514 other) {
        other.performTask514();
    }
}
