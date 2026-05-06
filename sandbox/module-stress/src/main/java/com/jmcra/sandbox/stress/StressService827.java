package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService827 {
    public String performTask827() {
        return "Task 827 result";
    }
    
    public void crossCall(StressService828 other) {
        other.performTask828();
    }
}
