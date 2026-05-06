package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService325 {
    public String performTask325() {
        return "Task 325 result";
    }
    
    public void crossCall(StressService326 other) {
        other.performTask326();
    }
}
