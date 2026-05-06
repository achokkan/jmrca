package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService97 {
    public String performTask97() {
        return "Task 97 result";
    }
    
    public void crossCall(StressService98 other) {
        other.performTask98();
    }
}
