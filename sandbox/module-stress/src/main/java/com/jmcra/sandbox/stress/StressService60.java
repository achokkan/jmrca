package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService60 {
    public String performTask60() {
        return "Task 60 result";
    }
    
    public void crossCall(StressService61 other) {
        other.performTask61();
    }
}
