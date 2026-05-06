package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService254 {
    public String performTask254() {
        return "Task 254 result";
    }
    
    public void crossCall(StressService255 other) {
        other.performTask255();
    }
}
