package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService890 {
    public String performTask890() {
        return "Task 890 result";
    }
    
    public void crossCall(StressService891 other) {
        other.performTask891();
    }
}
