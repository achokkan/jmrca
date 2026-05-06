package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService333 {
    public String performTask333() {
        return "Task 333 result";
    }
    
    public void crossCall(StressService334 other) {
        other.performTask334();
    }
}
