param(
    [ValidateSet("beta", "stable")]
    [string]$CreateFly = "beta",
    [ValidateSet("26.2", "26.1.2")]
    [string]$Minecraft = "26.2"
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$java25Candidates = @()
if ($env:JAVA_HOME) { $java25Candidates += $env:JAVA_HOME }
$bundledJava = Join-Path $env:USERPROFILE ".cache\copycats-dev\jdk-25.0.4.1+1"
$java25Candidates += $bundledJava
$adoptium = Join-Path $env:ProgramFiles "Eclipse Adoptium"
$java25Candidates += Get-ChildItem -LiteralPath $adoptium -Directory -Filter "jdk-25*" -ErrorAction SilentlyContinue |
    Sort-Object Name -Descending |
    ForEach-Object { $_.FullName }

$java25Home = $null
foreach ($candidate in $java25Candidates) {
    $java = Join-Path $candidate "bin\java.exe"
    if (Test-Path -LiteralPath $java) {
        $versionText = (& $java -version 2>&1 | Out-String)
        if ($versionText -match 'version "25\.') {
            $java25Home = $candidate
            break
        }
    }
}
if (-not $java25Home) {
    throw "Java 25 is required. Install Eclipse Temurin JDK 25 or set JAVA_HOME to your Java 25 JDK directory."
}
$env:JAVA_HOME = $java25Home

$devArguments = @("runClient", "-Ptarget_version=$Minecraft", "--console=plain")
if ($Minecraft -eq "26.2") {
    $createVersion = if ($CreateFly -eq "beta") { "26.2-6.0.9-2" } else { "26.2-rc-2-6.0.9-1" }
    $devArguments += "-Pcreate_fly_version=$createVersion"
    $devArguments += "-Pdev_run_dir=run-dev-26.2-$CreateFly"
    Write-Host "Launching Minecraft 26.2 with Create Fly $createVersion and Copycats+ v1.18."
} else {
    $devArguments += "-Pdev_run_dir=run-dev-26.1.2-stable"
    Write-Host "Launching Minecraft 26.1.2 with Create Fly 6.0.9-4 and Copycats+ v1.18."
}
Write-Host "Use a creative test world. Cheats let you place blocks with /setblock."
Write-Host "The client uses a separate world/config/mods directory for this combination."

Push-Location -LiteralPath $projectRoot
try {
    & (Join-Path $projectRoot "gradlew.bat") @devArguments
    if ($LASTEXITCODE -ne 0) {
        throw "Development client exited with code $LASTEXITCODE. See its logs/latest.log."
    }
} finally {
    Pop-Location
}
