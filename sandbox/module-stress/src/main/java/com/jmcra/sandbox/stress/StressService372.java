package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService372 {
    public String performTask372() {
        return "Task 372 result";
    }
    
    public void crossCall(StressService373 other) {
        other.performTask373();
    }
}
