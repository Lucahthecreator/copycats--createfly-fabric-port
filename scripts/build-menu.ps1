param(
    [Parameter(Mandatory = $true)]
    [ValidateSet("1.21.11", "26.1+", "26.2-beta", "26.2-compat", "all")]
    [string]$Target
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$dist = Join-Path $projectRoot "dist"

function Set-JavaHome([int]$Major) {
    $currentJava = if ($env:JAVA_HOME) { Join-Path $env:JAVA_HOME "bin\java.exe" } else { $null }
    if ($currentJava -and (Test-Path -LiteralPath $currentJava)) {
        $versionText = (& $currentJava -version 2>&1 | Out-String)
        if ($versionText -match "version `"$Major\.") {
            return
        }
    }

    $adoptium = Join-Path $env:ProgramFiles "Eclipse Adoptium"
    $jdk = Get-ChildItem -LiteralPath $adoptium -Directory -Filter "jdk-$Major*" -ErrorAction SilentlyContinue |
        Sort-Object Name -Descending |
        Select-Object -First 1
    if (-not $jdk -or -not (Test-Path -LiteralPath (Join-Path $jdk.FullName "bin\java.exe"))) {
        throw "Java $Major was not found. Install Eclipse Temurin JDK $Major or set JAVA_HOME to that JDK."
    }
    $env:JAVA_HOME = $jdk.FullName
}

function Build-Target([string]$Name) {
    $javaMajor = 25
    $buildDirectory = $projectRoot
    $gradleArguments = @("clean", "build", "--console=plain")
    $distName = $null

    switch ($Name) {
        "1.21.11" {
            $javaMajor = 21
            $buildDirectory = Join-Path $projectRoot "backport-1.21.11"
            $gradleArguments = @("-p", "backport-1.21.11", "clean", "build", "--console=plain")
            $distName = "Copycats-CreateFly-1.21.11-beta.2.jar"
        }
        "26.1+" {
            $gradleArguments += @("-Ptarget_version=26.1.2")
            $distName = "Copycats-CreateFly-26.1+-v1.16.jar"
        }
        "26.2-beta" {
            $gradleArguments += @("-Ptarget_version=26.2", "-Pcreate_fly_version=26.2-6.0.9-2")
            $distName = "Copycats-CreateFly-26.2-beta-v1.16.jar"
        }
        "26.2-compat" {
            $gradleArguments += @("-Ptarget_version=26.2", "-Pcreate_fly_version=26.2-rc-2-6.0.9-1")
            $distName = "Copycats-CreateFly-26.2-compat-v1.16.jar"
        }
        default { throw "Unknown build target: $Name" }
    }

    Set-JavaHome $javaMajor
    Write-Host "Building $Name with Java $javaMajor..." -ForegroundColor Cyan
    Push-Location -LiteralPath $projectRoot
    try {
        & (Join-Path $projectRoot "gradlew.bat") @gradleArguments
        if ($LASTEXITCODE -ne 0) {
            throw "Gradle build for $Name failed with exit code $LASTEXITCODE."
        }
    } finally {
        Pop-Location
    }

    $builtJar = Get-ChildItem -LiteralPath (Join-Path $buildDirectory "build\libs") -Filter "*.jar" -File |
        Where-Object { $_.Name -notlike "*-sources.jar" } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1
    if (-not $builtJar) {
        throw "Gradle completed for $Name but no installable JAR was found."
    }

    New-Item -ItemType Directory -Path $dist -Force | Out-Null
    $destination = Join-Path $dist $distName
    Copy-Item -LiteralPath $builtJar.FullName -Destination $destination -Force
    Write-Host "Created $destination" -ForegroundColor Green
}

try {
    if ($Target -eq "all") {
        foreach ($name in @("1.21.11", "26.1+", "26.2-beta", "26.2-compat")) {
            Build-Target $name
        }
    } else {
        Build-Target $Target
    }
} catch {
    Write-Error $_
    exit 1
}
