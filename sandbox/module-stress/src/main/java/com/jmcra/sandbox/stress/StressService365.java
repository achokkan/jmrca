package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService365 {
    public String performTask365() {
        return "Task 365 result";
    }
    
    public void crossCall(StressService366 other) {
        other.performTask366();
    }
}
