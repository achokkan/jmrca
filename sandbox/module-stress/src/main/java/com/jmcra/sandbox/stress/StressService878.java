package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService878 {
    public String performTask878() {
        return "Task 878 result";
    }
    
    public void crossCall(StressService879 other) {
        other.performTask879();
    }
}
