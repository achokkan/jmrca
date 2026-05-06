package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService7 {
    public String performTask7() {
        return "Task 7 result";
    }
    
    public void crossCall(StressService8 other) {
        other.performTask8();
    }
}
