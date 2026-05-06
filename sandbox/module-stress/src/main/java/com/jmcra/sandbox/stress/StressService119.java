package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService119 {
    public String performTask119() {
        return "Task 119 result";
    }
    
    public void crossCall(StressService120 other) {
        other.performTask120();
    }
}
