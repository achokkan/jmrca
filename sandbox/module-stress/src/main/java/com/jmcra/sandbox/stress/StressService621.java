package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService621 {
    public String performTask621() {
        return "Task 621 result";
    }
    
    public void crossCall(StressService622 other) {
        other.performTask622();
    }
}
