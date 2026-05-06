package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService390 {
    public String performTask390() {
        return "Task 390 result";
    }
    
    public void crossCall(StressService391 other) {
        other.performTask391();
    }
}
