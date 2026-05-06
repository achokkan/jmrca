package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService779 {
    public String performTask779() {
        return "Task 779 result";
    }
    
    public void crossCall(StressService780 other) {
        other.performTask780();
    }
}
