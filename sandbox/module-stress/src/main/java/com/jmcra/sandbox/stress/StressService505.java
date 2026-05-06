package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService505 {
    public String performTask505() {
        return "Task 505 result";
    }
    
    public void crossCall(StressService506 other) {
        other.performTask506();
    }
}
