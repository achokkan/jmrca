package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService247 {
    public String performTask247() {
        return "Task 247 result";
    }
    
    public void crossCall(StressService248 other) {
        other.performTask248();
    }
}
