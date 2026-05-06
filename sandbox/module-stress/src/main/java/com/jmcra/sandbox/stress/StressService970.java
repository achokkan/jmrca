package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService970 {
    public String performTask970() {
        return "Task 970 result";
    }
    
    public void crossCall(StressService971 other) {
        other.performTask971();
    }
}
