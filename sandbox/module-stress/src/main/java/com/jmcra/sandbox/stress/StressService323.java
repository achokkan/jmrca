package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService323 {
    public String performTask323() {
        return "Task 323 result";
    }
    
    public void crossCall(StressService324 other) {
        other.performTask324();
    }
}
