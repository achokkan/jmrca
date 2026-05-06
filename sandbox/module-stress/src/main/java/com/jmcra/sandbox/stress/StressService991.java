package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService991 {
    public String performTask991() {
        return "Task 991 result";
    }
    
    public void crossCall(StressService992 other) {
        other.performTask992();
    }
}
