package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService400 {
    public String performTask400() {
        return "Task 400 result";
    }
    
    public void crossCall(StressService401 other) {
        other.performTask401();
    }
}
