package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService237 {
    public String performTask237() {
        return "Task 237 result";
    }
    
    public void crossCall(StressService238 other) {
        other.performTask238();
    }
}
