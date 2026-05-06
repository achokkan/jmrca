package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService302 {
    public String performTask302() {
        return "Task 302 result";
    }
    
    public void crossCall(StressService303 other) {
        other.performTask303();
    }
}
