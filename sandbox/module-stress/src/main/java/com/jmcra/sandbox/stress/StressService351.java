package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService351 {
    public String performTask351() {
        return "Task 351 result";
    }
    
    public void crossCall(StressService352 other) {
        other.performTask352();
    }
}
