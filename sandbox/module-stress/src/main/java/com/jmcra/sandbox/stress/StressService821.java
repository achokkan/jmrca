package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService821 {
    public String performTask821() {
        return "Task 821 result";
    }
    
    public void crossCall(StressService822 other) {
        other.performTask822();
    }
}
