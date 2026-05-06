package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService42 {
    public String performTask42() {
        return "Task 42 result";
    }
    
    public void crossCall(StressService43 other) {
        other.performTask43();
    }
}
