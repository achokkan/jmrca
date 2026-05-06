package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService25 {
    public String performTask25() {
        return "Task 25 result";
    }
    
    public void crossCall(StressService26 other) {
        other.performTask26();
    }
}
