package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService49 {
    public String performTask49() {
        return "Task 49 result";
    }
    
    public void crossCall(StressService50 other) {
        other.performTask50();
    }
}
