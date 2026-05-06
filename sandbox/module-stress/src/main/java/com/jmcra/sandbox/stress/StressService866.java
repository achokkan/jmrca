package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService866 {
    public String performTask866() {
        return "Task 866 result";
    }
    
    public void crossCall(StressService867 other) {
        other.performTask867();
    }
}
