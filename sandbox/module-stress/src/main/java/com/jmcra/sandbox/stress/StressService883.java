package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService883 {
    public String performTask883() {
        return "Task 883 result";
    }
    
    public void crossCall(StressService884 other) {
        other.performTask884();
    }
}
