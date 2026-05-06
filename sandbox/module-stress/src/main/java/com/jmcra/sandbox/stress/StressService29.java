package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService29 {
    public String performTask29() {
        return "Task 29 result";
    }
    
    public void crossCall(StressService30 other) {
        other.performTask30();
    }
}
