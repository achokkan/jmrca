package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService131 {
    public String performTask131() {
        return "Task 131 result";
    }
    
    public void crossCall(StressService132 other) {
        other.performTask132();
    }
}
