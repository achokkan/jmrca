package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService240 {
    public String performTask240() {
        return "Task 240 result";
    }
    
    public void crossCall(StressService241 other) {
        other.performTask241();
    }
}
