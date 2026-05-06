package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService789 {
    public String performTask789() {
        return "Task 789 result";
    }
    
    public void crossCall(StressService790 other) {
        other.performTask790();
    }
}
