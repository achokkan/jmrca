package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService825 {
    public String performTask825() {
        return "Task 825 result";
    }
    
    public void crossCall(StressService826 other) {
        other.performTask826();
    }
}
