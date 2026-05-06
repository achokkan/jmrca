package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService822 {
    public String performTask822() {
        return "Task 822 result";
    }
    
    public void crossCall(StressService823 other) {
        other.performTask823();
    }
}
