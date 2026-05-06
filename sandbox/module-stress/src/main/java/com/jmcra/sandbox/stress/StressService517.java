package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService517 {
    public String performTask517() {
        return "Task 517 result";
    }
    
    public void crossCall(StressService518 other) {
        other.performTask518();
    }
}
