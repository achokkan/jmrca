package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService396 {
    public String performTask396() {
        return "Task 396 result";
    }
    
    public void crossCall(StressService397 other) {
        other.performTask397();
    }
}
