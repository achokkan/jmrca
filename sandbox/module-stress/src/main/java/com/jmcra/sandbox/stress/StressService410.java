package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService410 {
    public String performTask410() {
        return "Task 410 result";
    }
    
    public void crossCall(StressService411 other) {
        other.performTask411();
    }
}
