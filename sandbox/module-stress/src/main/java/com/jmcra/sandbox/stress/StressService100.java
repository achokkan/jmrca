package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService100 {
    public String performTask100() {
        return "Task 100 result";
    }
    
    public void crossCall(StressService101 other) {
        other.performTask101();
    }
}
