package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService184 {
    public String performTask184() {
        return "Task 184 result";
    }
    
    public void crossCall(StressService185 other) {
        other.performTask185();
    }
}
