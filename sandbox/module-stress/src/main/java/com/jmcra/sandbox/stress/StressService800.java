package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService800 {
    public String performTask800() {
        return "Task 800 result";
    }
    
    public void crossCall(StressService801 other) {
        other.performTask801();
    }
}
