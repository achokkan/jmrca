package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService95 {
    public String performTask95() {
        return "Task 95 result";
    }
    
    public void crossCall(StressService96 other) {
        other.performTask96();
    }
}
