package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService363 {
    public String performTask363() {
        return "Task 363 result";
    }
    
    public void crossCall(StressService364 other) {
        other.performTask364();
    }
}
