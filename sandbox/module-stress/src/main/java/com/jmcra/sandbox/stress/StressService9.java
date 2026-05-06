package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService9 {
    public String performTask9() {
        return "Task 9 result";
    }
    
    public void crossCall(StressService10 other) {
        other.performTask10();
    }
}
