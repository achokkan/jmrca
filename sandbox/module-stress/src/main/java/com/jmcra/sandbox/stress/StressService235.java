package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService235 {
    public String performTask235() {
        return "Task 235 result";
    }
    
    public void crossCall(StressService236 other) {
        other.performTask236();
    }
}
