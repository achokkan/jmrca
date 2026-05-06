package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService781 {
    public String performTask781() {
        return "Task 781 result";
    }
    
    public void crossCall(StressService782 other) {
        other.performTask782();
    }
}
