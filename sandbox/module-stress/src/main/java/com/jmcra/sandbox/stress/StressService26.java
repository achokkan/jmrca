package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService26 {
    public String performTask26() {
        return "Task 26 result";
    }
    
    public void crossCall(StressService27 other) {
        other.performTask27();
    }
}
