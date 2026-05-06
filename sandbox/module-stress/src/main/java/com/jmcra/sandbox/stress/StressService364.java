package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService364 {
    public String performTask364() {
        return "Task 364 result";
    }
    
    public void crossCall(StressService365 other) {
        other.performTask365();
    }
}
