package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService180 {
    public String performTask180() {
        return "Task 180 result";
    }
    
    public void crossCall(StressService181 other) {
        other.performTask181();
    }
}
