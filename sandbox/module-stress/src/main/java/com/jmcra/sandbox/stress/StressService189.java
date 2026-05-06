package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService189 {
    public String performTask189() {
        return "Task 189 result";
    }
    
    public void crossCall(StressService190 other) {
        other.performTask190();
    }
}
