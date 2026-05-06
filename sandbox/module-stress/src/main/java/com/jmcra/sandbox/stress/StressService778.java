package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService778 {
    public String performTask778() {
        return "Task 778 result";
    }
    
    public void crossCall(StressService779 other) {
        other.performTask779();
    }
}
