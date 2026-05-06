package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService457 {
    public String performTask457() {
        return "Task 457 result";
    }
    
    public void crossCall(StressService458 other) {
        other.performTask458();
    }
}
