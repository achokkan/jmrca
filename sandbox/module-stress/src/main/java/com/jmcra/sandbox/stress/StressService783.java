package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService783 {
    public String performTask783() {
        return "Task 783 result";
    }
    
    public void crossCall(StressService784 other) {
        other.performTask784();
    }
}
