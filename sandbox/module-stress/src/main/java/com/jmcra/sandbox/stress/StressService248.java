package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService248 {
    public String performTask248() {
        return "Task 248 result";
    }
    
    public void crossCall(StressService249 other) {
        other.performTask249();
    }
}
