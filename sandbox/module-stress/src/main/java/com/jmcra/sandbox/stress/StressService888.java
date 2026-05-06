package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService888 {
    public String performTask888() {
        return "Task 888 result";
    }
    
    public void crossCall(StressService889 other) {
        other.performTask889();
    }
}
