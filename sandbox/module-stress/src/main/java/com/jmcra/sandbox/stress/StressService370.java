package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService370 {
    public String performTask370() {
        return "Task 370 result";
    }
    
    public void crossCall(StressService371 other) {
        other.performTask371();
    }
}
