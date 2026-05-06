package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService951 {
    public String performTask951() {
        return "Task 951 result";
    }
    
    public void crossCall(StressService952 other) {
        other.performTask952();
    }
}
