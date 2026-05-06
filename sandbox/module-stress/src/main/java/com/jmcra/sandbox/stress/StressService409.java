package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService409 {
    public String performTask409() {
        return "Task 409 result";
    }
    
    public void crossCall(StressService410 other) {
        other.performTask410();
    }
}
