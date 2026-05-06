package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService578 {
    public String performTask578() {
        return "Task 578 result";
    }
    
    public void crossCall(StressService579 other) {
        other.performTask579();
    }
}
