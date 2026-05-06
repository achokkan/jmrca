package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService350 {
    public String performTask350() {
        return "Task 350 result";
    }
    
    public void crossCall(StressService351 other) {
        other.performTask351();
    }
}
