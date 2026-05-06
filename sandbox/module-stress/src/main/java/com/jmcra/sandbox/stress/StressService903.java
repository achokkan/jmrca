package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService903 {
    public String performTask903() {
        return "Task 903 result";
    }
    
    public void crossCall(StressService904 other) {
        other.performTask904();
    }
}
