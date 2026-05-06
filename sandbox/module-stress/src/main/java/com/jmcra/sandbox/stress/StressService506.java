package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService506 {
    public String performTask506() {
        return "Task 506 result";
    }
    
    public void crossCall(StressService507 other) {
        other.performTask507();
    }
}
