package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService315 {
    public String performTask315() {
        return "Task 315 result";
    }
    
    public void crossCall(StressService316 other) {
        other.performTask316();
    }
}
