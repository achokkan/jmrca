package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService16 {
    public String performTask16() {
        return "Task 16 result";
    }
    
    public void crossCall(StressService17 other) {
        other.performTask17();
    }
}
