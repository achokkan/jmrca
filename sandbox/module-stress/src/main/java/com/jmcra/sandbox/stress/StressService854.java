package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService854 {
    public String performTask854() {
        return "Task 854 result";
    }
    
    public void crossCall(StressService855 other) {
        other.performTask855();
    }
}
