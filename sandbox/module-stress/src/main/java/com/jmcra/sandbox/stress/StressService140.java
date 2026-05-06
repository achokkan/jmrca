package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService140 {
    public String performTask140() {
        return "Task 140 result";
    }
    
    public void crossCall(StressService141 other) {
        other.performTask141();
    }
}
