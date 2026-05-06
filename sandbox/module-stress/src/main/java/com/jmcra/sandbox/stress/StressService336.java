package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService336 {
    public String performTask336() {
        return "Task 336 result";
    }
    
    public void crossCall(StressService337 other) {
        other.performTask337();
    }
}
