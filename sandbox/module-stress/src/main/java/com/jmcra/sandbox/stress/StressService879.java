package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService879 {
    public String performTask879() {
        return "Task 879 result";
    }
    
    public void crossCall(StressService880 other) {
        other.performTask880();
    }
}
