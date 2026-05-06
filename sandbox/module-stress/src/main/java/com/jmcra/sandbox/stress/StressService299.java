package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService299 {
    public String performTask299() {
        return "Task 299 result";
    }
    
    public void crossCall(StressService300 other) {
        other.performTask300();
    }
}
