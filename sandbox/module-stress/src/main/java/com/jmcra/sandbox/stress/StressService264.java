package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService264 {
    public String performTask264() {
        return "Task 264 result";
    }
    
    public void crossCall(StressService265 other) {
        other.performTask265();
    }
}
