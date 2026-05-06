package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService568 {
    public String performTask568() {
        return "Task 568 result";
    }
    
    public void crossCall(StressService569 other) {
        other.performTask569();
    }
}
