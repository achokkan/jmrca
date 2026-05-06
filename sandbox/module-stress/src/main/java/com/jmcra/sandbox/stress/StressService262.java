package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService262 {
    public String performTask262() {
        return "Task 262 result";
    }
    
    public void crossCall(StressService263 other) {
        other.performTask263();
    }
}
