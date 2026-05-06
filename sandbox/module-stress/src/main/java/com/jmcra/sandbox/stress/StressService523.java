package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService523 {
    public String performTask523() {
        return "Task 523 result";
    }
    
    public void crossCall(StressService524 other) {
        other.performTask524();
    }
}
