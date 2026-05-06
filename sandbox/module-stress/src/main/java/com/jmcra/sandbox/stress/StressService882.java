package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService882 {
    public String performTask882() {
        return "Task 882 result";
    }
    
    public void crossCall(StressService883 other) {
        other.performTask883();
    }
}
