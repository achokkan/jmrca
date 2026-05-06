package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService887 {
    public String performTask887() {
        return "Task 887 result";
    }
    
    public void crossCall(StressService888 other) {
        other.performTask888();
    }
}
