package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService820 {
    public String performTask820() {
        return "Task 820 result";
    }
    
    public void crossCall(StressService821 other) {
        other.performTask821();
    }
}
