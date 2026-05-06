package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService533 {
    public String performTask533() {
        return "Task 533 result";
    }
    
    public void crossCall(StressService534 other) {
        other.performTask534();
    }
}
