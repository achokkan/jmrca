package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService666 {
    public String performTask666() {
        return "Task 666 result";
    }
    
    public void crossCall(StressService667 other) {
        other.performTask667();
    }
}
