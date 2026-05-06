package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService203 {
    public String performTask203() {
        return "Task 203 result";
    }
    
    public void crossCall(StressService204 other) {
        other.performTask204();
    }
}
