package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService236 {
    public String performTask236() {
        return "Task 236 result";
    }
    
    public void crossCall(StressService237 other) {
        other.performTask237();
    }
}
