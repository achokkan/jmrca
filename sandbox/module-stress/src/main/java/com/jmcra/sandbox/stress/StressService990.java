package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService990 {
    public String performTask990() {
        return "Task 990 result";
    }
    
    public void crossCall(StressService991 other) {
        other.performTask991();
    }
}
