package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService276 {
    public String performTask276() {
        return "Task 276 result";
    }
    
    public void crossCall(StressService277 other) {
        other.performTask277();
    }
}
