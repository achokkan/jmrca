package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService90 {
    public String performTask90() {
        return "Task 90 result";
    }
    
    public void crossCall(StressService91 other) {
        other.performTask91();
    }
}
