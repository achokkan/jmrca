package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService80 {
    public String performTask80() {
        return "Task 80 result";
    }
    
    public void crossCall(StressService81 other) {
        other.performTask81();
    }
}
