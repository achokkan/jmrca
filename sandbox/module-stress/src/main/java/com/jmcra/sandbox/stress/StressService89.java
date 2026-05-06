package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService89 {
    public String performTask89() {
        return "Task 89 result";
    }
    
    public void crossCall(StressService90 other) {
        other.performTask90();
    }
}
