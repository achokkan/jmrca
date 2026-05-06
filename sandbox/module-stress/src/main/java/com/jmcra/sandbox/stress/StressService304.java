package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService304 {
    public String performTask304() {
        return "Task 304 result";
    }
    
    public void crossCall(StressService305 other) {
        other.performTask305();
    }
}
