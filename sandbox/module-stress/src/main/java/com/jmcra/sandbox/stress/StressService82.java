package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService82 {
    public String performTask82() {
        return "Task 82 result";
    }
    
    public void crossCall(StressService83 other) {
        other.performTask83();
    }
}
