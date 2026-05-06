package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService306 {
    public String performTask306() {
        return "Task 306 result";
    }
    
    public void crossCall(StressService307 other) {
        other.performTask307();
    }
}
