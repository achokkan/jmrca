package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService880 {
    public String performTask880() {
        return "Task 880 result";
    }
    
    public void crossCall(StressService881 other) {
        other.performTask881();
    }
}
