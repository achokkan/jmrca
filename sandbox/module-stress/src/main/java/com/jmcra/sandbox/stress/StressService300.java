package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService300 {
    public String performTask300() {
        return "Task 300 result";
    }
    
    public void crossCall(StressService301 other) {
        other.performTask301();
    }
}
