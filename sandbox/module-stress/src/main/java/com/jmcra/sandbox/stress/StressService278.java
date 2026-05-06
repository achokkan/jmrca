package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService278 {
    public String performTask278() {
        return "Task 278 result";
    }
    
    public void crossCall(StressService279 other) {
        other.performTask279();
    }
}
