package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService132 {
    public String performTask132() {
        return "Task 132 result";
    }
    
    public void crossCall(StressService133 other) {
        other.performTask133();
    }
}
