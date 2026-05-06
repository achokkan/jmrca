package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService562 {
    public String performTask562() {
        return "Task 562 result";
    }
    
    public void crossCall(StressService563 other) {
        other.performTask563();
    }
}
