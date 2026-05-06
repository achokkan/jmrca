package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService604 {
    public String performTask604() {
        return "Task 604 result";
    }
    
    public void crossCall(StressService605 other) {
        other.performTask605();
    }
}
