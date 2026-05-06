package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService801 {
    public String performTask801() {
        return "Task 801 result";
    }
    
    public void crossCall(StressService802 other) {
        other.performTask802();
    }
}
