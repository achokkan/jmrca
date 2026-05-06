package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService977 {
    public String performTask977() {
        return "Task 977 result";
    }
    
    public void crossCall(StressService978 other) {
        other.performTask978();
    }
}
