package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService231 {
    public String performTask231() {
        return "Task 231 result";
    }
    
    public void crossCall(StressService232 other) {
        other.performTask232();
    }
}
