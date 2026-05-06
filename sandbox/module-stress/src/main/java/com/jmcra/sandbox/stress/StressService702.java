package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService702 {
    public String performTask702() {
        return "Task 702 result";
    }
    
    public void crossCall(StressService703 other) {
        other.performTask703();
    }
}
