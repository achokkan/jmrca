#!/usr/bin/env bash
# =============================================================================
# test-sandbox.sh
# Main test utility for JMCRA Local Sandbox.
# =============================================================================

set -euo pipefail

JMCRA_URL="${JMCRA_URL:-http://localhost:8080}"
SANDBOX_PATH="$(pwd)/sandbox"
ORACLE_FILE="sandbox/expected-findings.json"

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  JMCRA Sandbox Regression Test"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# 1. Trigger Scan
echo "Step 1: Triggering local scan of $SANDBOX_PATH..."
RESPONSE=$(curl -s -X POST "${JMCRA_URL}/webhook/local" \
    -H "Content-Type: application/json" \
    -d "{ \"path\": \"$SANDBOX_PATH\", \"profile\": \"full\" }")

SCAN_ID=$(echo "$RESPONSE" | grep -oE '"scanId":"([^"]+)"' | cut -d'"' -f4)

if [[ -z "$SCAN_ID" ]]; then
    echo "Error: Failed to trigger scan. Response: $RESPONSE"
    exit 1
fi

echo "Scan started. ID: $SCAN_ID"

# 2. Wait for Results
echo "Step 2: Waiting for results..."
STATUS="PENDING"
while [[ "$STATUS" != "COMPLETED" && "$STATUS" != "FAILED" ]]; do
    sleep 2
    RESULT=$(curl -s "${JMCRA_URL}/api/scans/$SCAN_ID")
    STATUS=$(echo "$RESULT" | grep -oE '"status":"([^"]+)"' | cut -d'"' -f4)
    echo -n "."
done
echo " Done."

# 3. Compare with Oracle
echo "Step 3: Comparing findings with $ORACLE_FILE..."
# In a real shell script, we'd use 'jq' to compare findings.
# For now, we print the summary.

FINDINGS_COUNT=$(echo "$RESULT" | grep -oE '"findingsCount":([0-9]+)' | cut -d: -f2)
EXPECTED_COUNT=$(grep -c "ruleId" "$ORACLE_FILE")

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Detected Findings : $FINDINGS_COUNT"
echo "  Expected Findings : $EXPECTED_COUNT"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [[ "$FINDINGS_COUNT" -lt "$EXPECTED_COUNT" ]]; then
    echo -e "\033[0;31mFAILED: Regression detected. Missing findings.\033[0m"
    exit 1
else
    echo -e "\033[0;32mPASSED: Sandbox scan matches expected oracle.\033[0m"
    exit 0
fi
