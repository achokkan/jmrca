package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService782 {
    public String performTask782() {
        return "Task 782 result";
    }
    
    public void crossCall(StressService783 other) {
        other.performTask783();
    }
}
