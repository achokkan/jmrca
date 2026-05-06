package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService296 {
    public String performTask296() {
        return "Task 296 result";
    }
    
    public void crossCall(StressService297 other) {
        other.performTask297();
    }
}
