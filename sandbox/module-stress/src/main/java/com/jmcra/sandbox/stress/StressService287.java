package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService287 {
    public String performTask287() {
        return "Task 287 result";
    }
    
    public void crossCall(StressService288 other) {
        other.performTask288();
    }
}
