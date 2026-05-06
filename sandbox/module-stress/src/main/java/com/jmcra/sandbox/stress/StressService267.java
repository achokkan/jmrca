package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService267 {
    public String performTask267() {
        return "Task 267 result";
    }
    
    public void crossCall(StressService268 other) {
        other.performTask268();
    }
}
