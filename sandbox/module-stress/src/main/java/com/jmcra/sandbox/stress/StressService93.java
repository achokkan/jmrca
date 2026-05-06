package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService93 {
    public String performTask93() {
        return "Task 93 result";
    }
    
    public void crossCall(StressService94 other) {
        other.performTask94();
    }
}
