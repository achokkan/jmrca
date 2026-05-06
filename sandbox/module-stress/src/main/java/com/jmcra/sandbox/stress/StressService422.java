package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService422 {
    public String performTask422() {
        return "Task 422 result";
    }
    
    public void crossCall(StressService423 other) {
        other.performTask423();
    }
}
