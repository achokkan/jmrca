package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService711 {
    public String performTask711() {
        return "Task 711 result";
    }
    
    public void crossCall(StressService712 other) {
        other.performTask712();
    }
}
