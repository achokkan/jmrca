package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService950 {
    public String performTask950() {
        return "Task 950 result";
    }
    
    public void crossCall(StressService951 other) {
        other.performTask951();
    }
}
