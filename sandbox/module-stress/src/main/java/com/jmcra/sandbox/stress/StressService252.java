package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService252 {
    public String performTask252() {
        return "Task 252 result";
    }
    
    public void crossCall(StressService253 other) {
        other.performTask253();
    }
}
