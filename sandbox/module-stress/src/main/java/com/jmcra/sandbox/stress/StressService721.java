package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService721 {
    public String performTask721() {
        return "Task 721 result";
    }
    
    public void crossCall(StressService722 other) {
        other.performTask722();
    }
}
