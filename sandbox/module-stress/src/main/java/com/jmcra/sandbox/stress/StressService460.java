package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService460 {
    public String performTask460() {
        return "Task 460 result";
    }
    
    public void crossCall(StressService461 other) {
        other.performTask461();
    }
}
