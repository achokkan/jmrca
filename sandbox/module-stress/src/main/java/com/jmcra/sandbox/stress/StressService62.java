package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService62 {
    public String performTask62() {
        return "Task 62 result";
    }
    
    public void crossCall(StressService63 other) {
        other.performTask63();
    }
}
