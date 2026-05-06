package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService6 {
    public String performTask6() {
        return "Task 6 result";
    }
    
    public void crossCall(StressService7 other) {
        other.performTask7();
    }
}
