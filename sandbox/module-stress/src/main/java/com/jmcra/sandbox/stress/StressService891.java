package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService891 {
    public String performTask891() {
        return "Task 891 result";
    }
    
    public void crossCall(StressService892 other) {
        other.performTask892();
    }
}
