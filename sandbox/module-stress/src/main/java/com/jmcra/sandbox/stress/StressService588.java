package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService588 {
    public String performTask588() {
        return "Task 588 result";
    }
    
    public void crossCall(StressService589 other) {
        other.performTask589();
    }
}
