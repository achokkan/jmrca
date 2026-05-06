package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService303 {
    public String performTask303() {
        return "Task 303 result";
    }
    
    public void crossCall(StressService304 other) {
        other.performTask304();
    }
}
