package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService380 {
    public String performTask380() {
        return "Task 380 result";
    }
    
    public void crossCall(StressService381 other) {
        other.performTask381();
    }
}
