# generate-stress-test.ps1
# Generates 1,000 Java classes to test JMCRA performance.

$BaseDir = "sandbox/module-stress/src/main/java/com/jmcra/sandbox/stress"
if (!(Test-Path $BaseDir)) {
    New-Item -ItemType Directory -Force -Path $BaseDir
}

Write-Host "Generating 1,000 Java classes for stress testing..."

for ($i = 1; $i -le 1000; $i++) {
    $Next = ($i % 1000) + 1
    $Content = @"
package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService$i {
    public String performTask$i() {
        return "Task $i result";
    }
    
    public void crossCall(StressService$Next other) {
        other.performTask$Next();
    }
}
"@
    Set-Content -Path "$BaseDir/StressService$i.java" -Value $Content
}

Write-Host "Done. 1,000 classes generated in $BaseDir"
