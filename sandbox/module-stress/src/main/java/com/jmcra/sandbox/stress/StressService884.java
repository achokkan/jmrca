package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService884 {
    public String performTask884() {
        return "Task 884 result";
    }
    
    public void crossCall(StressService885 other) {
        other.performTask885();
    }
}
