package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService374 {
    public String performTask374() {
        return "Task 374 result";
    }
    
    public void crossCall(StressService375 other) {
        other.performTask375();
    }
}
