package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService92 {
    public String performTask92() {
        return "Task 92 result";
    }
    
    public void crossCall(StressService93 other) {
        other.performTask93();
    }
}
