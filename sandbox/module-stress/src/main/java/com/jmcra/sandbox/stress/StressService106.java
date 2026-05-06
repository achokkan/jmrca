package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService106 {
    public String performTask106() {
        return "Task 106 result";
    }
    
    public void crossCall(StressService107 other) {
        other.performTask107();
    }
}
