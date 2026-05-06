package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService510 {
    public String performTask510() {
        return "Task 510 result";
    }
    
    public void crossCall(StressService511 other) {
        other.performTask511();
    }
}
