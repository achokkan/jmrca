package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService177 {
    public String performTask177() {
        return "Task 177 result";
    }
    
    public void crossCall(StressService178 other) {
        other.performTask178();
    }
}
