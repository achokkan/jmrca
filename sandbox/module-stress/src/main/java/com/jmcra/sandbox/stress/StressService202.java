package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService202 {
    public String performTask202() {
        return "Task 202 result";
    }
    
    public void crossCall(StressService203 other) {
        other.performTask203();
    }
}
