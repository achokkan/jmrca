package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService976 {
    public String performTask976() {
        return "Task 976 result";
    }
    
    public void crossCall(StressService977 other) {
        other.performTask977();
    }
}
