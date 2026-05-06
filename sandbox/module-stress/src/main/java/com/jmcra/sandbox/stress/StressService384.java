package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService384 {
    public String performTask384() {
        return "Task 384 result";
    }
    
    public void crossCall(StressService385 other) {
        other.performTask385();
    }
}
