package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService708 {
    public String performTask708() {
        return "Task 708 result";
    }
    
    public void crossCall(StressService709 other) {
        other.performTask709();
    }
}
