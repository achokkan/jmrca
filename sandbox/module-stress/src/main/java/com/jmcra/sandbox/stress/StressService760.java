package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService760 {
    public String performTask760() {
        return "Task 760 result";
    }
    
    public void crossCall(StressService761 other) {
        other.performTask761();
    }
}
