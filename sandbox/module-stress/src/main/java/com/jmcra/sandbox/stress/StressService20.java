package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService20 {
    public String performTask20() {
        return "Task 20 result";
    }
    
    public void crossCall(StressService21 other) {
        other.performTask21();
    }
}
