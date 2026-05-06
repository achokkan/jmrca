package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService797 {
    public String performTask797() {
        return "Task 797 result";
    }
    
    public void crossCall(StressService798 other) {
        other.performTask798();
    }
}
