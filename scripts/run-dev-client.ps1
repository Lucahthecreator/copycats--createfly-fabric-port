param(
    [ValidateSet("beta", "stable")]
    [string]$CreateFly = "beta",
    [ValidateSet("26.2", "26.1.2")]
    [string]$Minecraft = "26.2"
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$bundledJava = Join-Path $env:USERPROFILE ".cache\copycats-dev\jdk-25.0.4.1+1"
if (-not $env:JAVA_HOME -and (Test-Path -LiteralPath (Join-Path $bundledJava "bin\java.exe"))) {
    $env:JAVA_HOME = $bundledJava
}
if (-not $env:JAVA_HOME -or -not (Test-Path -LiteralPath (Join-Path $env:JAVA_HOME "bin\java.exe"))) {
    throw "Java 25 is required. Set JAVA_HOME to your Java 25 JDK directory."
}

$devArguments = @("runClient", "-Ptarget_version=$Minecraft", "--console=plain")
if ($Minecraft -eq "26.2") {
    $createVersion = if ($CreateFly -eq "beta") { "26.2-6.0.9-2" } else { "26.2-rc-2-6.0.9-1" }
    $devArguments += "-Pcreate_fly_version=$createVersion"
    $devArguments += "-Pdev_run_dir=run-dev-26.2-$CreateFly"
    Write-Host "Launching Minecraft 26.2 with Create Fly $createVersion and Copycats+ v1.16."
} else {
    $devArguments += "-Pdev_run_dir=run-dev-26.1.2-stable"
    Write-Host "Launching Minecraft 26.1.2 with Create Fly 6.0.9-4 and Copycats+ v1.16."
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
