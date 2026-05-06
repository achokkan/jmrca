package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService298 {
    public String performTask298() {
        return "Task 298 result";
    }
    
    public void crossCall(StressService299 other) {
        other.performTask299();
    }
}
