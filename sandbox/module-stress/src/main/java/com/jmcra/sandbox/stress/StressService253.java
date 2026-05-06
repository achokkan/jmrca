package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService253 {
    public String performTask253() {
        return "Task 253 result";
    }
    
    public void crossCall(StressService254 other) {
        other.performTask254();
    }
}
