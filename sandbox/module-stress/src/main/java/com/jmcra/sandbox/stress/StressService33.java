package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService33 {
    public String performTask33() {
        return "Task 33 result";
    }
    
    public void crossCall(StressService34 other) {
        other.performTask34();
    }
}
