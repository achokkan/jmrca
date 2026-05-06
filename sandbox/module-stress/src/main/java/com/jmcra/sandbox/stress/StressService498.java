package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService498 {
    public String performTask498() {
        return "Task 498 result";
    }
    
    public void crossCall(StressService499 other) {
        other.performTask499();
    }
}
