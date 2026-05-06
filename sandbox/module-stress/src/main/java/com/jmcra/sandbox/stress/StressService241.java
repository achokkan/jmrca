package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService241 {
    public String performTask241() {
        return "Task 241 result";
    }
    
    public void crossCall(StressService242 other) {
        other.performTask242();
    }
}
