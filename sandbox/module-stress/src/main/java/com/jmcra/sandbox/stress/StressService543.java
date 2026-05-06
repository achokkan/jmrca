package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService543 {
    public String performTask543() {
        return "Task 543 result";
    }
    
    public void crossCall(StressService544 other) {
        other.performTask544();
    }
}
