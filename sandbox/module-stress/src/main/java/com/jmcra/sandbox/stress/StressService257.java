package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService257 {
    public String performTask257() {
        return "Task 257 result";
    }
    
    public void crossCall(StressService258 other) {
        other.performTask258();
    }
}
