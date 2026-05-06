package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService38 {
    public String performTask38() {
        return "Task 38 result";
    }
    
    public void crossCall(StressService39 other) {
        other.performTask39();
    }
}
