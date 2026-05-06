package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService567 {
    public String performTask567() {
        return "Task 567 result";
    }
    
    public void crossCall(StressService568 other) {
        other.performTask568();
    }
}
