package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService401 {
    public String performTask401() {
        return "Task 401 result";
    }
    
    public void crossCall(StressService402 other) {
        other.performTask402();
    }
}
