package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService99 {
    public String performTask99() {
        return "Task 99 result";
    }
    
    public void crossCall(StressService100 other) {
        other.performTask100();
    }
}
