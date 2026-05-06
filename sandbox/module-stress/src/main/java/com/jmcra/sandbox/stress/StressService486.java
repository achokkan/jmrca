package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService486 {
    public String performTask486() {
        return "Task 486 result";
    }
    
    public void crossCall(StressService487 other) {
        other.performTask487();
    }
}
