package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService11 {
    public String performTask11() {
        return "Task 11 result";
    }
    
    public void crossCall(StressService12 other) {
        other.performTask12();
    }
}
