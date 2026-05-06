package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService806 {
    public String performTask806() {
        return "Task 806 result";
    }
    
    public void crossCall(StressService807 other) {
        other.performTask807();
    }
}
