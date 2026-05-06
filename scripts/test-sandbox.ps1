# test-sandbox.ps1
# JMCRA Sandbox Regression Test Utility (PowerShell)

$AgentUrl = "http://localhost:8080"
$SandboxPath = (Get-Item "sandbox").FullName
$OracleFile = "sandbox/expected-findings.json"

Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "  JMCRA Sandbox Regression Test"
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# 1. Trigger Scan
Write-Host "Step 1: Triggering local scan of $SandboxPath..."
$Payload = @{
    path = $SandboxPath
    profile = "full"
} | ConvertTo-Json

try {
    $Response = Invoke-RestMethod -Uri "$AgentUrl/webhook/local" -Method Post -Body $Payload -ContentType "application/json"
    $ScanId = $Response.scanId
    Write-Host "Scan started. ID: $ScanId" -ForegroundColor Green
}
catch {
    Write-Host "Error: Failed to trigger scan. Is the agent running at $AgentUrl?" -ForegroundColor Red
    exit 1
}

# 2. Wait for Completion
Write-Host "Step 2: Waiting for results..."
$Status = "PENDING"
while ($Status -ne "COMPLETED" -and $Status -ne "FAILED") {
    Start-Sleep -Seconds 2
    $Scan = Invoke-RestMethod -Uri "$AgentUrl/api/scans/$ScanId"
    $Status = $Scan.status
    Write-Host -NoNewline "."
}
Write-Host " Done."

# 3. Compare with Oracle
Write-Host "Step 3: Comparing findings..."
$Report = Invoke-RestMethod -Uri "$AgentUrl/api/scans/$ScanId/report"
$DetectedCount = $Report.findings.Count
$ExpectedFindings = Get-Content $OracleFile | ConvertFrom-Json
$ExpectedCount = $ExpectedFindings.expectedFindings.Count

Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
Write-Host "  Detected Findings : $DetectedCount"
Write-Host "  Expected Findings : $ExpectedCount"
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if ($DetectedCount -lt $ExpectedCount) {
    Write-Host "FAILED: Regression detected. Some vulnerabilities were missed." -ForegroundColor Red
    
    # List missing rules (simplified comparison)
    $DetectedRules = $Report.findings.ruleId
    foreach ($exp in $ExpectedFindings.expectedFindings) {
        if ($DetectedRules -notcontains $exp.ruleId) {
            Write-Host "  [-] Missing: $($exp.ruleId) in $($exp.file)"
        }
    }
}
else {
    Write-Host "PASSED: Sandbox scan matches expected oracle." -ForegroundColor Green
    Write-Host "`nFull report available at: $AgentUrl/api/scans/$ScanId/report"
}
