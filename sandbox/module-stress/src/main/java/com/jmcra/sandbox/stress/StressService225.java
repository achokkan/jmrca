package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService225 {
    public String performTask225() {
        return "Task 225 result";
    }
    
    public void crossCall(StressService226 other) {
        other.performTask226();
    }
}
