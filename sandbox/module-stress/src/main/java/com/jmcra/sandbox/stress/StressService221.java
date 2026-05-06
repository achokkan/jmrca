package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService221 {
    public String performTask221() {
        return "Task 221 result";
    }
    
    public void crossCall(StressService222 other) {
        other.performTask222();
    }
}
