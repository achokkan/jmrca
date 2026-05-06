package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService877 {
    public String performTask877() {
        return "Task 877 result";
    }
    
    public void crossCall(StressService878 other) {
        other.performTask878();
    }
}
