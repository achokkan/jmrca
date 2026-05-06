package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService258 {
    public String performTask258() {
        return "Task 258 result";
    }
    
    public void crossCall(StressService259 other) {
        other.performTask259();
    }
}
