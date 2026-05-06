package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService50 {
    public String performTask50() {
        return "Task 50 result";
    }
    
    public void crossCall(StressService51 other) {
        other.performTask51();
    }
}
