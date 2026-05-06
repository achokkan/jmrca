package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService307 {
    public String performTask307() {
        return "Task 307 result";
    }
    
    public void crossCall(StressService308 other) {
        other.performTask308();
    }
}
