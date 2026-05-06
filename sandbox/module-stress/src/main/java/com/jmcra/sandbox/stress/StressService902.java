package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService902 {
    public String performTask902() {
        return "Task 902 result";
    }
    
    public void crossCall(StressService903 other) {
        other.performTask903();
    }
}
