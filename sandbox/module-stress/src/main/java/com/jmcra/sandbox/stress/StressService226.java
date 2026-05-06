package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService226 {
    public String performTask226() {
        return "Task 226 result";
    }
    
    public void crossCall(StressService227 other) {
        other.performTask227();
    }
}
