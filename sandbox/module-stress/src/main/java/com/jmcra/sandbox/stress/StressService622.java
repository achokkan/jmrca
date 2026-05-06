package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService622 {
    public String performTask622() {
        return "Task 622 result";
    }
    
    public void crossCall(StressService623 other) {
        other.performTask623();
    }
}
