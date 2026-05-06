package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService91 {
    public String performTask91() {
        return "Task 91 result";
    }
    
    public void crossCall(StressService92 other) {
        other.performTask92();
    }
}
