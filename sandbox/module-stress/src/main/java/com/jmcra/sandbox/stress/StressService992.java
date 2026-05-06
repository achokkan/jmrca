package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService992 {
    public String performTask992() {
        return "Task 992 result";
    }
    
    public void crossCall(StressService993 other) {
        other.performTask993();
    }
}
