package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService19 {
    public String performTask19() {
        return "Task 19 result";
    }
    
    public void crossCall(StressService20 other) {
        other.performTask20();
    }
}
