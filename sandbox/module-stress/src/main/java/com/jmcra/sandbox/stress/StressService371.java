package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService371 {
    public String performTask371() {
        return "Task 371 result";
    }
    
    public void crossCall(StressService372 other) {
        other.performTask372();
    }
}
