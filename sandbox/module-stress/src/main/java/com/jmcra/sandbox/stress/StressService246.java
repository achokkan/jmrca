package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService246 {
    public String performTask246() {
        return "Task 246 result";
    }
    
    public void crossCall(StressService247 other) {
        other.performTask247();
    }
}
