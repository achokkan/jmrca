package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService18 {
    public String performTask18() {
        return "Task 18 result";
    }
    
    public void crossCall(StressService19 other) {
        other.performTask19();
    }
}
