package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService956 {
    public String performTask956() {
        return "Task 956 result";
    }
    
    public void crossCall(StressService957 other) {
        other.performTask957();
    }
}
