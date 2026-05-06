package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService17 {
    public String performTask17() {
        return "Task 17 result";
    }
    
    public void crossCall(StressService18 other) {
        other.performTask18();
    }
}
