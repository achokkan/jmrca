package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService130 {
    public String performTask130() {
        return "Task 130 result";
    }
    
    public void crossCall(StressService131 other) {
        other.performTask131();
    }
}
