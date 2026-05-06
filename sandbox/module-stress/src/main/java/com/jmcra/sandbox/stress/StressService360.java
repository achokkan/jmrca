package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService360 {
    public String performTask360() {
        return "Task 360 result";
    }
    
    public void crossCall(StressService361 other) {
        other.performTask361();
    }
}
