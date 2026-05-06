package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService346 {
    public String performTask346() {
        return "Task 346 result";
    }
    
    public void crossCall(StressService347 other) {
        other.performTask347();
    }
}
