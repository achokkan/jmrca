package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService379 {
    public String performTask379() {
        return "Task 379 result";
    }
    
    public void crossCall(StressService380 other) {
        other.performTask380();
    }
}
