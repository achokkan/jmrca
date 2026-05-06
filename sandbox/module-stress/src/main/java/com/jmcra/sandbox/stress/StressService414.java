package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService414 {
    public String performTask414() {
        return "Task 414 result";
    }
    
    public void crossCall(StressService415 other) {
        other.performTask415();
    }
}
