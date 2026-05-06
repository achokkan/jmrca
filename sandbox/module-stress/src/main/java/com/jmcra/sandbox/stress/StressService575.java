package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService575 {
    public String performTask575() {
        return "Task 575 result";
    }
    
    public void crossCall(StressService576 other) {
        other.performTask576();
    }
}
