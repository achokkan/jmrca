package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService961 {
    public String performTask961() {
        return "Task 961 result";
    }
    
    public void crossCall(StressService962 other) {
        other.performTask962();
    }
}
