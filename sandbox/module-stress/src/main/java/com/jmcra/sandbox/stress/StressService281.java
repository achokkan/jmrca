package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService281 {
    public String performTask281() {
        return "Task 281 result";
    }
    
    public void crossCall(StressService282 other) {
        other.performTask282();
    }
}
