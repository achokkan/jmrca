package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService331 {
    public String performTask331() {
        return "Task 331 result";
    }
    
    public void crossCall(StressService332 other) {
        other.performTask332();
    }
}
