package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService936 {
    public String performTask936() {
        return "Task 936 result";
    }
    
    public void crossCall(StressService937 other) {
        other.performTask937();
    }
}
