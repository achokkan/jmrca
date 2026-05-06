package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService200 {
    public String performTask200() {
        return "Task 200 result";
    }
    
    public void crossCall(StressService201 other) {
        other.performTask201();
    }
}
