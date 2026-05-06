package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService892 {
    public String performTask892() {
        return "Task 892 result";
    }
    
    public void crossCall(StressService893 other) {
        other.performTask893();
    }
}
