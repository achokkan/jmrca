package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService963 {
    public String performTask963() {
        return "Task 963 result";
    }
    
    public void crossCall(StressService964 other) {
        other.performTask964();
    }
}
