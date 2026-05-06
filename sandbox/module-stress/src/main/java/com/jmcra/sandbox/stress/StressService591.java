package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService591 {
    public String performTask591() {
        return "Task 591 result";
    }
    
    public void crossCall(StressService592 other) {
        other.performTask592();
    }
}
