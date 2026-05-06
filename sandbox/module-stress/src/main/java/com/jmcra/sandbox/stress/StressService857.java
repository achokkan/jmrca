package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService857 {
    public String performTask857() {
        return "Task 857 result";
    }
    
    public void crossCall(StressService858 other) {
        other.performTask858();
    }
}
