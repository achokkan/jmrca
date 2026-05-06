package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService920 {
    public String performTask920() {
        return "Task 920 result";
    }
    
    public void crossCall(StressService921 other) {
        other.performTask921();
    }
}
