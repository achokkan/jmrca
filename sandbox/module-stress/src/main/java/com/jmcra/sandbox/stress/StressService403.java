package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService403 {
    public String performTask403() {
        return "Task 403 result";
    }
    
    public void crossCall(StressService404 other) {
        other.performTask404();
    }
}
