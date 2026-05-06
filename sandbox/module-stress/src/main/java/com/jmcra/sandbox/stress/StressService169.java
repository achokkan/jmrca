package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService169 {
    public String performTask169() {
        return "Task 169 result";
    }
    
    public void crossCall(StressService170 other) {
        other.performTask170();
    }
}
