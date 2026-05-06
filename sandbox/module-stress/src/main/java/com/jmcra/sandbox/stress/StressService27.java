package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService27 {
    public String performTask27() {
        return "Task 27 result";
    }
    
    public void crossCall(StressService28 other) {
        other.performTask28();
    }
}
