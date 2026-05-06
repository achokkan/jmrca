package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService308 {
    public String performTask308() {
        return "Task 308 result";
    }
    
    public void crossCall(StressService309 other) {
        other.performTask309();
    }
}
