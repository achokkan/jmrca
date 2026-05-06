package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService501 {
    public String performTask501() {
        return "Task 501 result";
    }
    
    public void crossCall(StressService502 other) {
        other.performTask502();
    }
}
