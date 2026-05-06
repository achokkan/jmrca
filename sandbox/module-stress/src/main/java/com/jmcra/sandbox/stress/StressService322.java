package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService322 {
    public String performTask322() {
        return "Task 322 result";
    }
    
    public void crossCall(StressService323 other) {
        other.performTask323();
    }
}
