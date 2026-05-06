package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService381 {
    public String performTask381() {
        return "Task 381 result";
    }
    
    public void crossCall(StressService382 other) {
        other.performTask382();
    }
}
