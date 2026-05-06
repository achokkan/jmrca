package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService88 {
    public String performTask88() {
        return "Task 88 result";
    }
    
    public void crossCall(StressService89 other) {
        other.performTask89();
    }
}
