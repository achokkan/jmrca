package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService87 {
    public String performTask87() {
        return "Task 87 result";
    }
    
    public void crossCall(StressService88 other) {
        other.performTask88();
    }
}
