#!/usr/bin/env bash
# =============================================================================
# generate-stress-test.sh
# Generates 1,000 Java classes to test JMCRA performance (Section 9).
# =============================================================================

set -euo pipefail

BASE_DIR="sandbox/module-stress/src/main/java/com/jmcra/sandbox/stress"
mkdir -p "$BASE_DIR"

echo "Generating 1,000 Java classes for stress testing..."

for i in {1..1000}; do
    cat <<EOF > "${BASE_DIR}/StressService${i}.java"
package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService${i} {
    public String performTask${i}() {
        return "Task ${i} result";
    }
    
    public void crossCall(StressService$(( (i % 1000) + 1 )) other) {
        other.performTask$(( (i % 1000) + 1 ))();
    }
}
EOF
done

echo "Done. 1,000 classes generated in $BASE_DIR"
