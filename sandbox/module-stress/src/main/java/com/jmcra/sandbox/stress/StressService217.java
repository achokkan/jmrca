package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService217 {
    public String performTask217() {
        return "Task 217 result";
    }
    
    public void crossCall(StressService218 other) {
        other.performTask218();
    }
}
