package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService480 {
    public String performTask480() {
        return "Task 480 result";
    }
    
    public void crossCall(StressService481 other) {
        other.performTask481();
    }
}
