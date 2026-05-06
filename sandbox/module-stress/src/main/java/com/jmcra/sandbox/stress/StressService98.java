package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService98 {
    public String performTask98() {
        return "Task 98 result";
    }
    
    public void crossCall(StressService99 other) {
        other.performTask99();
    }
}
