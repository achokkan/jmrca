package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService78 {
    public String performTask78() {
        return "Task 78 result";
    }
    
    public void crossCall(StressService79 other) {
        other.performTask79();
    }
}
