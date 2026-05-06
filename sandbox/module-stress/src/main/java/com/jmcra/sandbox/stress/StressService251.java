package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService251 {
    public String performTask251() {
        return "Task 251 result";
    }
    
    public void crossCall(StressService252 other) {
        other.performTask252();
    }
}
