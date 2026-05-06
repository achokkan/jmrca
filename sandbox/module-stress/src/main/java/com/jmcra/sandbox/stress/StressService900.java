package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService900 {
    public String performTask900() {
        return "Task 900 result";
    }
    
    public void crossCall(StressService901 other) {
        other.performTask901();
    }
}
