package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService771 {
    public String performTask771() {
        return "Task 771 result";
    }
    
    public void crossCall(StressService772 other) {
        other.performTask772();
    }
}
