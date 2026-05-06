package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService37 {
    public String performTask37() {
        return "Task 37 result";
    }
    
    public void crossCall(StressService38 other) {
        other.performTask38();
    }
}
