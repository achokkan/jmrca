package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService151 {
    public String performTask151() {
        return "Task 151 result";
    }
    
    public void crossCall(StressService152 other) {
        other.performTask152();
    }
}
