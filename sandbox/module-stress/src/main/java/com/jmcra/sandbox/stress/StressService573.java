package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService573 {
    public String performTask573() {
        return "Task 573 result";
    }
    
    public void crossCall(StressService574 other) {
        other.performTask574();
    }
}
