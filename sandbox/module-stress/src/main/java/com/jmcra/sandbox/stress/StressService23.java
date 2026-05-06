package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService23 {
    public String performTask23() {
        return "Task 23 result";
    }
    
    public void crossCall(StressService24 other) {
        other.performTask24();
    }
}
