package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService294 {
    public String performTask294() {
        return "Task 294 result";
    }
    
    public void crossCall(StressService295 other) {
        other.performTask295();
    }
}
