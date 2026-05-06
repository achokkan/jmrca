# JMCRA — Local Scan Utility
# Usage: .\scripts\scan-local.ps1 [-Path <directory>] [-Profile <profile>]

param (
    [string]$Path = (Get-Item .).FullName,
    [string]$Profile = "DEFAULT"
)

$AgentUrl = "http://localhost:8080/webhook/local"

Write-Host "--- JMCRA Local Scan ---" -ForegroundColor Cyan
Write-Host "Target Path: $Path"
Write-Host "Scan Profile: $Profile"
Write-Host "Agent URL: $AgentUrl"

$Payload = @{
    path = $Path
    branch = "local-branch"
    profile = $Profile
} | ConvertTo-Json

try {
    Write-Host "`nSending request to JMCRA Agent..."
    $Response = Invoke-RestMethod -Uri $AgentUrl -Method Post -Body $Payload -ContentType "application/json"
    
    Write-Host "`nSuccessfully triggered scan!" -ForegroundColor Green
    Write-Host "Scan ID: $($Response.scanId)"
    Write-Host "Pipeline is now running in the background."
    Write-Host "Check the agent logs for progress."
}
catch {
    Write-Host "`nError triggering scan: $($_.Exception.Message)" -ForegroundColor Red
}
