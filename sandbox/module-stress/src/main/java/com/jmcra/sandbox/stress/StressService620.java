package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService620 {
    public String performTask620() {
        return "Task 620 result";
    }
    
    public void crossCall(StressService621 other) {
        other.performTask621();
    }
}
