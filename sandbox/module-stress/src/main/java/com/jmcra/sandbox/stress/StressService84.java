package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService84 {
    public String performTask84() {
        return "Task 84 result";
    }
    
    public void crossCall(StressService85 other) {
        other.performTask85();
    }
}
