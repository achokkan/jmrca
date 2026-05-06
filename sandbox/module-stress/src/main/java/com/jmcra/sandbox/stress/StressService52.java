package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService52 {
    public String performTask52() {
        return "Task 52 result";
    }
    
    public void crossCall(StressService53 other) {
        other.performTask53();
    }
}
