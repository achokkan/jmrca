package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService83 {
    public String performTask83() {
        return "Task 83 result";
    }
    
    public void crossCall(StressService84 other) {
        other.performTask84();
    }
}
