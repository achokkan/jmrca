package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService451 {
    public String performTask451() {
        return "Task 451 result";
    }
    
    public void crossCall(StressService452 other) {
        other.performTask452();
    }
}
