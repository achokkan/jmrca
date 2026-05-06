package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService305 {
    public String performTask305() {
        return "Task 305 result";
    }
    
    public void crossCall(StressService306 other) {
        other.performTask306();
    }
}
