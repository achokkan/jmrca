package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService347 {
    public String performTask347() {
        return "Task 347 result";
    }
    
    public void crossCall(StressService348 other) {
        other.performTask348();
    }
}
