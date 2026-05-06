package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService477 {
    public String performTask477() {
        return "Task 477 result";
    }
    
    public void crossCall(StressService478 other) {
        other.performTask478();
    }
}
