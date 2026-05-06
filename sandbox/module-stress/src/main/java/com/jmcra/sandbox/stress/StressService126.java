package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService126 {
    public String performTask126() {
        return "Task 126 result";
    }
    
    public void crossCall(StressService127 other) {
        other.performTask127();
    }
}
