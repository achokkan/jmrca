package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService599 {
    public String performTask599() {
        return "Task 599 result";
    }
    
    public void crossCall(StressService600 other) {
        other.performTask600();
    }
}
