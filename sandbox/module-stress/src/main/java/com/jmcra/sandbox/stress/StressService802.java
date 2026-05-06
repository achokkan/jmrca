package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService802 {
    public String performTask802() {
        return "Task 802 result";
    }
    
    public void crossCall(StressService803 other) {
        other.performTask803();
    }
}
