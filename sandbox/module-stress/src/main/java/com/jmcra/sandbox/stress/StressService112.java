package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService112 {
    public String performTask112() {
        return "Task 112 result";
    }
    
    public void crossCall(StressService113 other) {
        other.performTask113();
    }
}
