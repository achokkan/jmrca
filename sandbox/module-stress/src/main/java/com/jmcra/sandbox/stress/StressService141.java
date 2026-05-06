package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService141 {
    public String performTask141() {
        return "Task 141 result";
    }
    
    public void crossCall(StressService142 other) {
        other.performTask142();
    }
}
