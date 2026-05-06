package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService788 {
    public String performTask788() {
        return "Task 788 result";
    }
    
    public void crossCall(StressService789 other) {
        other.performTask789();
    }
}
