package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService798 {
    public String performTask798() {
        return "Task 798 result";
    }
    
    public void crossCall(StressService799 other) {
        other.performTask799();
    }
}
