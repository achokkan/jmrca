package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService234 {
    public String performTask234() {
        return "Task 234 result";
    }
    
    public void crossCall(StressService235 other) {
        other.performTask235();
    }
}
