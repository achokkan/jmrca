package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService901 {
    public String performTask901() {
        return "Task 901 result";
    }
    
    public void crossCall(StressService902 other) {
        other.performTask902();
    }
}
