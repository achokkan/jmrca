package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService787 {
    public String performTask787() {
        return "Task 787 result";
    }
    
    public void crossCall(StressService788 other) {
        other.performTask788();
    }
}
