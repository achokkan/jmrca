package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService972 {
    public String performTask972() {
        return "Task 972 result";
    }
    
    public void crossCall(StressService973 other) {
        other.performTask973();
    }
}
