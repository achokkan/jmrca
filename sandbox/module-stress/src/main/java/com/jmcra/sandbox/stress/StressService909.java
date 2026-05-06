package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService909 {
    public String performTask909() {
        return "Task 909 result";
    }
    
    public void crossCall(StressService910 other) {
        other.performTask910();
    }
}
