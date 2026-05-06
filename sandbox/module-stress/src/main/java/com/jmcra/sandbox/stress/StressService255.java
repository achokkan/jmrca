package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService255 {
    public String performTask255() {
        return "Task 255 result";
    }
    
    public void crossCall(StressService256 other) {
        other.performTask256();
    }
}
