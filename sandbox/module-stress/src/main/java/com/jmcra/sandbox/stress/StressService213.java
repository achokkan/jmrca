package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService213 {
    public String performTask213() {
        return "Task 213 result";
    }
    
    public void crossCall(StressService214 other) {
        other.performTask214();
    }
}
