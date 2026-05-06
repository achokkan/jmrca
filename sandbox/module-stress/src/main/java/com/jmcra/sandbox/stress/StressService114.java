package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService114 {
    public String performTask114() {
        return "Task 114 result";
    }
    
    public void crossCall(StressService115 other) {
        other.performTask115();
    }
}
