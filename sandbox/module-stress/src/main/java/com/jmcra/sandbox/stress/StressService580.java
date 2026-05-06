package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService580 {
    public String performTask580() {
        return "Task 580 result";
    }
    
    public void crossCall(StressService581 other) {
        other.performTask581();
    }
}
