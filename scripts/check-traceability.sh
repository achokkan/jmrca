#!/usr/bin/env bash
# =============================================================================
# check-traceability.sh
# JMCRA Spec Traceability Matrix Validator
#
# Scans all @RuleContractTest and @PipelineContractTest annotations in the
# test source tree, extracts Spec Clause IDs (SPC-NNN), and cross-checks
# against the known SPC ID catalog. Outputs a traceability matrix CSV and
# fails the build if any SPC ID is MISSING (uncovered by tests).
#
# Spec: Section 7A.5 (Spec Drift Detection — Traceability Matrix CI Check)
#       Section 7A.6 (Traceability Matrix Format)
#
# Usage:
#   ./scripts/check-traceability.sh [--update]
#
# Exit codes:
#   0 — All SPC IDs are covered (COVERED or N/A)
#   1 — One or more SPC IDs are MISSING — build blocked
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
TEST_SRC="${PROJECT_ROOT}/src/test/java"
OUTPUT_CSV="${PROJECT_ROOT}/docs/traceability-matrix.csv"
CATALOG="${PROJECT_ROOT}/rules/catalog.json"

# Colours
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Colour

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  JMCRA Spec Traceability Check"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# =============================================================================
# Step 1 — Extract all @specClause values from test source
# =============================================================================
declare -A SPC_TO_TESTS

# Find all specClause = "SPC-xxx" occurrences in test Java files
while IFS= read -r line; do
    # Extract SPC IDs from specClause = "SPC-001,SPC-002" or specClause = "SPC-001"
    if echo "$line" | grep -qE 'specClause\s*=\s*"([^"]+)"'; then
        spc_ids=$(echo "$line" | grep -oE '"SPC-[0-9,]+"' | tr -d '"' | tr ',' '\n')
        test_file=$(echo "$line" | awk -F: '{print $1}')
        for spc_id in $spc_ids; do
            spc_id=$(echo "$spc_id" | xargs)  # trim
            if [[ "$spc_id" =~ ^SPC-[0-9]+$ ]]; then
                SPC_TO_TESTS[$spc_id]="${SPC_TO_TESTS[$spc_id]:-}${test_file},"
            fi
        done
    fi
done < <(grep -rn 'specClause' "${TEST_SRC}" 2>/dev/null || true)

echo "Found ${#SPC_TO_TESTS[@]} unique SPC IDs covered by tests."

# =============================================================================
# Step 2 — Define the canonical SPC ID catalog
#           (In production: parsed from spec index; here: statically defined)
# =============================================================================
CANONICAL_SPCS=(
    "SPC-007:Ingest HMAC rejection and ScanRequest shape"
    "SPC-008:ScanStarted event emitted after ingest"
    "SPC-010:ParseIndex AST completeness"
    "SPC-011:AnalysisDispatch parallel isolation"
    "SPC-012:RankDedupe dedup key and ranking"
    "SPC-013:Delivery fan-out to all targets"
    "SPC-031:Finding schema compliance (FindingSchema v1)"
    "SPC-042:SEC-001 positive: inline secret detection"
    "SPC-043:SEC-001 negative: @Value injection not flagged"
    "SPC-044:SEC-002 positive: SQL injection via concatenation"
    "SPC-045:SEC-002 negative: parameterised query"
    "SPC-046:SEC-003 positive: weak JWT secret"
    "SPC-047:SEC-003 negative: strong JWT secret with expiry"
    "SPC-048:SEC-004 positive: missing @PreAuthorize"
    "SPC-049:SEC-004 negative: @PreAuthorize present"
    "SPC-070:DEP-001 positive: CVE dependency"
    "SPC-071:DEP-001 negative: clean dependency"
    "SPC-072:DEP-005 javax.inject import flagged"
    "SPC-088:RES-001 circuit breaker absence"
    "SPC-089:DAT-001 N+1 lazy fetch"
    "SPC-099:OBS-001 trace propagation Feign"
)

# =============================================================================
# Step 3 — Build the traceability matrix CSV
# =============================================================================
mkdir -p "$(dirname "${OUTPUT_CSV}")"

echo "spc_id,description,covered,test_file" > "${OUTPUT_CSV}"

MISSING=0
COVERED=0
TOTAL=${#CANONICAL_SPCS[@]}

for entry in "${CANONICAL_SPCS[@]}"; do
    spc_id="${entry%%:*}"
    description="${entry#*:}"
    test_file="${SPC_TO_TESTS[$spc_id]:-}"

    if [[ -n "$test_file" ]]; then
        status="COVERED"
        echo "${spc_id},\"${description}\",COVERED,\"${test_file%,}\"" >> "${OUTPUT_CSV}"
        ((COVERED++)) || true
    else
        status="MISSING"
        echo "${spc_id},\"${description}\",MISSING,\"\"" >> "${OUTPUT_CSV}"
        ((MISSING++)) || true
        echo -e "${RED}  ✗ MISSING: ${spc_id} — ${description}${NC}"
    fi
done

# =============================================================================
# Step 4 — Summary and exit
# =============================================================================
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo -e "  Total SPC IDs : ${TOTAL}"
echo -e "  ${GREEN}Covered        : ${COVERED}${NC}"
echo -e "  ${RED}Missing        : ${MISSING}${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Traceability matrix written to: ${OUTPUT_CSV}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [[ ${MISSING} -gt 0 ]]; then
    echo ""
    echo -e "${RED}BUILD FAILED: ${MISSING} SPC ID(s) have no test coverage.${NC}"
    echo "Add @RuleContractTest or @PipelineContractTest with the missing SPC IDs"
    echo "before implementing the spec clause. See SDD Section 7A.7."
    exit 1
fi

echo ""
echo -e "${GREEN}✓ Traceability check passed — all ${TOTAL} SPC IDs are covered.${NC}"
exit 0
