package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService962 {
    public String performTask962() {
        return "Task 962 result";
    }
    
    public void crossCall(StressService963 other) {
        other.performTask963();
    }
}
