package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService421 {
    public String performTask421() {
        return "Task 421 result";
    }
    
    public void crossCall(StressService422 other) {
        other.performTask422();
    }
}
