package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService271 {
    public String performTask271() {
        return "Task 271 result";
    }
    
    public void crossCall(StressService272 other) {
        other.performTask272();
    }
}
