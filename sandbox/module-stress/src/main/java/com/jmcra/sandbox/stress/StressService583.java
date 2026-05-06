package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService583 {
    public String performTask583() {
        return "Task 583 result";
    }
    
    public void crossCall(StressService584 other) {
        other.performTask584();
    }
}
