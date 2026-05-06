package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService127 {
    public String performTask127() {
        return "Task 127 result";
    }
    
    public void crossCall(StressService128 other) {
        other.performTask128();
    }
}
