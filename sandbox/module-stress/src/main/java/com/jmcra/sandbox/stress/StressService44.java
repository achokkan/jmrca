package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService44 {
    public String performTask44() {
        return "Task 44 result";
    }
    
    public void crossCall(StressService45 other) {
        other.performTask45();
    }
}
