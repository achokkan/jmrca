package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService886 {
    public String performTask886() {
        return "Task 886 result";
    }
    
    public void crossCall(StressService887 other) {
        other.performTask887();
    }
}
