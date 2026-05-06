package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService872 {
    public String performTask872() {
        return "Task 872 result";
    }
    
    public void crossCall(StressService873 other) {
        other.performTask873();
    }
}
