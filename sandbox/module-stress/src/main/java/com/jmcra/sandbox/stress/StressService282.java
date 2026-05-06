package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService282 {
    public String performTask282() {
        return "Task 282 result";
    }
    
    public void crossCall(StressService283 other) {
        other.performTask283();
    }
}
