package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService415 {
    public String performTask415() {
        return "Task 415 result";
    }
    
    public void crossCall(StressService416 other) {
        other.performTask416();
    }
}
