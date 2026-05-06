package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService326 {
    public String performTask326() {
        return "Task 326 result";
    }
    
    public void crossCall(StressService327 other) {
        other.performTask327();
    }
}
