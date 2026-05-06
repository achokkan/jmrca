package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService205 {
    public String performTask205() {
        return "Task 205 result";
    }
    
    public void crossCall(StressService206 other) {
        other.performTask206();
    }
}
