package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService110 {
    public String performTask110() {
        return "Task 110 result";
    }
    
    public void crossCall(StressService111 other) {
        other.performTask111();
    }
}
