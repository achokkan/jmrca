package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService509 {
    public String performTask509() {
        return "Task 509 result";
    }
    
    public void crossCall(StressService510 other) {
        other.performTask510();
    }
}
