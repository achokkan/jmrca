package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService103 {
    public String performTask103() {
        return "Task 103 result";
    }
    
    public void crossCall(StressService104 other) {
        other.performTask104();
    }
}
