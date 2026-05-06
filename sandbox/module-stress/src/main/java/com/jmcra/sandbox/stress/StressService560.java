package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService560 {
    public String performTask560() {
        return "Task 560 result";
    }
    
    public void crossCall(StressService561 other) {
        other.performTask561();
    }
}
