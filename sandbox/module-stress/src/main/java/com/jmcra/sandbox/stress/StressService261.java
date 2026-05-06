package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService261 {
    public String performTask261() {
        return "Task 261 result";
    }
    
    public void crossCall(StressService262 other) {
        other.performTask262();
    }
}
