package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService113 {
    public String performTask113() {
        return "Task 113 result";
    }
    
    public void crossCall(StressService114 other) {
        other.performTask114();
    }
}
