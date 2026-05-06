package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService61 {
    public String performTask61() {
        return "Task 61 result";
    }
    
    public void crossCall(StressService62 other) {
        other.performTask62();
    }
}
