package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService507 {
    public String performTask507() {
        return "Task 507 result";
    }
    
    public void crossCall(StressService508 other) {
        other.performTask508();
    }
}
