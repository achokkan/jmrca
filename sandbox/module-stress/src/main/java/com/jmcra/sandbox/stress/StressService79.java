package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService79 {
    public String performTask79() {
        return "Task 79 result";
    }
    
    public void crossCall(StressService80 other) {
        other.performTask80();
    }
}
