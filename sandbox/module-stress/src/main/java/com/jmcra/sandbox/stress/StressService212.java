package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService212 {
    public String performTask212() {
        return "Task 212 result";
    }
    
    public void crossCall(StressService213 other) {
        other.performTask213();
    }
}
