package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService120 {
    public String performTask120() {
        return "Task 120 result";
    }
    
    public void crossCall(StressService121 other) {
        other.performTask121();
    }
}
