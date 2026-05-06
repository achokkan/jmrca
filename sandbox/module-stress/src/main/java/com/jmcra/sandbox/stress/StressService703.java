package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService703 {
    public String performTask703() {
        return "Task 703 result";
    }
    
    public void crossCall(StressService704 other) {
        other.performTask704();
    }
}
