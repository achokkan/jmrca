package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService320 {
    public String performTask320() {
        return "Task 320 result";
    }
    
    public void crossCall(StressService321 other) {
        other.performTask321();
    }
}
