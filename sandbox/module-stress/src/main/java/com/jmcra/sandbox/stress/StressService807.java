package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService807 {
    public String performTask807() {
        return "Task 807 result";
    }
    
    public void crossCall(StressService808 other) {
        other.performTask808();
    }
}
