package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService46 {
    public String performTask46() {
        return "Task 46 result";
    }
    
    public void crossCall(StressService47 other) {
        other.performTask47();
    }
}
