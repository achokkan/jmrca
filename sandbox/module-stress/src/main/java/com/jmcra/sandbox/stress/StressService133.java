package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService133 {
    public String performTask133() {
        return "Task 133 result";
    }
    
    public void crossCall(StressService134 other) {
        other.performTask134();
    }
}
