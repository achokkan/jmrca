package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService30 {
    public String performTask30() {
        return "Task 30 result";
    }
    
    public void crossCall(StressService31 other) {
        other.performTask31();
    }
}
