package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService223 {
    public String performTask223() {
        return "Task 223 result";
    }
    
    public void crossCall(StressService224 other) {
        other.performTask224();
    }
}
