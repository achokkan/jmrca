package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService201 {
    public String performTask201() {
        return "Task 201 result";
    }
    
    public void crossCall(StressService202 other) {
        other.performTask202();
    }
}
