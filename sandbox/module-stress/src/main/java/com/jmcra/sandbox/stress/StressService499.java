package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService499 {
    public String performTask499() {
        return "Task 499 result";
    }
    
    public void crossCall(StressService500 other) {
        other.performTask500();
    }
}
