package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService249 {
    public String performTask249() {
        return "Task 249 result";
    }
    
    public void crossCall(StressService250 other) {
        other.performTask250();
    }
}
