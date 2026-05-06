package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService492 {
    public String performTask492() {
        return "Task 492 result";
    }
    
    public void crossCall(StressService493 other) {
        other.performTask493();
    }
}
