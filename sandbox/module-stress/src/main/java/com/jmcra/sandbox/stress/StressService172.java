package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService172 {
    public String performTask172() {
        return "Task 172 result";
    }
    
    public void crossCall(StressService173 other) {
        other.performTask173();
    }
}
