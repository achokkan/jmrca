package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService35 {
    public String performTask35() {
        return "Task 35 result";
    }
    
    public void crossCall(StressService36 other) {
        other.performTask36();
    }
}
