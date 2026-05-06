package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService715 {
    public String performTask715() {
        return "Task 715 result";
    }
    
    public void crossCall(StressService716 other) {
        other.performTask716();
    }
}
