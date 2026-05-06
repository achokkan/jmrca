package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService94 {
    public String performTask94() {
        return "Task 94 result";
    }
    
    public void crossCall(StressService95 other) {
        other.performTask95();
    }
}
