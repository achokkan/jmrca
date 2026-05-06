package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService280 {
    public String performTask280() {
        return "Task 280 result";
    }
    
    public void crossCall(StressService281 other) {
        other.performTask281();
    }
}
