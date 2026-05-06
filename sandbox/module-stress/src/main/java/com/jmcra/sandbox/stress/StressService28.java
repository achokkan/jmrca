package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService28 {
    public String performTask28() {
        return "Task 28 result";
    }
    
    public void crossCall(StressService29 other) {
        other.performTask29();
    }
}
