package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService227 {
    public String performTask227() {
        return "Task 227 result";
    }
    
    public void crossCall(StressService228 other) {
        other.performTask228();
    }
}
