package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService443 {
    public String performTask443() {
        return "Task 443 result";
    }
    
    public void crossCall(StressService444 other) {
        other.performTask444();
    }
}
