package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService579 {
    public String performTask579() {
        return "Task 579 result";
    }
    
    public void crossCall(StressService580 other) {
        other.performTask580();
    }
}
