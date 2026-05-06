package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService881 {
    public String performTask881() {
        return "Task 881 result";
    }
    
    public void crossCall(StressService882 other) {
        other.performTask882();
    }
}
