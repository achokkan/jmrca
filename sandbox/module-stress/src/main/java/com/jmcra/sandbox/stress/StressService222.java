package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService222 {
    public String performTask222() {
        return "Task 222 result";
    }
    
    public void crossCall(StressService223 other) {
        other.performTask223();
    }
}
