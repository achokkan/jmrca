package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService978 {
    public String performTask978() {
        return "Task 978 result";
    }
    
    public void crossCall(StressService979 other) {
        other.performTask979();
    }
}
