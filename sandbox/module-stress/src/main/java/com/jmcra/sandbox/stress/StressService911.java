package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService911 {
    public String performTask911() {
        return "Task 911 result";
    }
    
    public void crossCall(StressService912 other) {
        other.performTask912();
    }
}
