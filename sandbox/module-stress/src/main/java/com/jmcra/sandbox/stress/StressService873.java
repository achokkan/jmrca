package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService873 {
    public String performTask873() {
        return "Task 873 result";
    }
    
    public void crossCall(StressService874 other) {
        other.performTask874();
    }
}
