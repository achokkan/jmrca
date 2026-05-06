package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService341 {
    public String performTask341() {
        return "Task 341 result";
    }
    
    public void crossCall(StressService342 other) {
        other.performTask342();
    }
}
