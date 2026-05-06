package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService500 {
    public String performTask500() {
        return "Task 500 result";
    }
    
    public void crossCall(StressService501 other) {
        other.performTask501();
    }
}
