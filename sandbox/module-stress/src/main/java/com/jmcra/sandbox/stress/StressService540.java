package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService540 {
    public String performTask540() {
        return "Task 540 result";
    }
    
    public void crossCall(StressService541 other) {
        other.performTask541();
    }
}
