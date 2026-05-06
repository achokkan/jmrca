package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService786 {
    public String performTask786() {
        return "Task 786 result";
    }
    
    public void crossCall(StressService787 other) {
        other.performTask787();
    }
}
