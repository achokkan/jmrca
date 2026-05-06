package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService835 {
    public String performTask835() {
        return "Task 835 result";
    }
    
    public void crossCall(StressService836 other) {
        other.performTask836();
    }
}
