package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService101 {
    public String performTask101() {
        return "Task 101 result";
    }
    
    public void crossCall(StressService102 other) {
        other.performTask102();
    }
}
