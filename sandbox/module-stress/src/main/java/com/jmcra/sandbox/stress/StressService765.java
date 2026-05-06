package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService765 {
    public String performTask765() {
        return "Task 765 result";
    }
    
    public void crossCall(StressService766 other) {
        other.performTask766();
    }
}
