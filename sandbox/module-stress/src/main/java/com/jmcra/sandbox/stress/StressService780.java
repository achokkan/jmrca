package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService780 {
    public String performTask780() {
        return "Task 780 result";
    }
    
    public void crossCall(StressService781 other) {
        other.performTask781();
    }
}
