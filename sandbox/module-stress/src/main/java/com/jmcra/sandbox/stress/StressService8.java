package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService8 {
    public String performTask8() {
        return "Task 8 result";
    }
    
    public void crossCall(StressService9 other) {
        other.performTask9();
    }
}
