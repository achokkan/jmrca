package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService104 {
    public String performTask104() {
        return "Task 104 result";
    }
    
    public void crossCall(StressService105 other) {
        other.performTask105();
    }
}
