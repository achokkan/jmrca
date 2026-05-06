package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService206 {
    public String performTask206() {
        return "Task 206 result";
    }
    
    public void crossCall(StressService207 other) {
        other.performTask207();
    }
}
