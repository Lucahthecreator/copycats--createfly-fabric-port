param(
    [ValidateSet("build", "client", "server", "gametest")]
    [string]$Action = "build",
    [string]$CreateFlyJar,
    [string]$CreateFlyProject
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$neoForgeProject = Join-Path $projectRoot "neoforge-26.3"

if (-not $CreateFlyProject) {
    $candidates = @(
        $env:CREATE_FLY_NEOFORGE_HOME,
        (Join-Path $projectRoot "..\ZP5\mods\create-fly-neoforge"),
        (Join-Path $projectRoot "..\..\..\ZP5\mods\create-fly-neoforge"),
        "C:\cfneo"
    ) | Where-Object { $_ }
    $CreateFlyProject = $candidates | Where-Object { Test-Path -LiteralPath (Join-Path $_ "interfaces.json") } | Select-Object -First 1
}

if (-not $CreateFlyJar -and $CreateFlyProject) {
    $libs = Join-Path $CreateFlyProject "build\libs"
    $CreateFlyJar = Get-ChildItem -LiteralPath $libs -Filter "create-fly-neoforge-*.jar" -File -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -notlike "*-sources.jar" } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1 -ExpandProperty FullName
}

if (-not $CreateFlyProject -or -not $CreateFlyJar) {
    throw "Build the Create Fly NeoForge 26.3 branch first. Set CREATE_FLY_NEOFORGE_HOME to its checkout or pass -CreateFlyProject and -CreateFlyJar. Source: https://github.com/zoom-packers/Create-Fly/tree/neoforge-26.3"
}

$interfaces = Join-Path $CreateFlyProject "interfaces.json"
if (-not (Test-Path -LiteralPath $interfaces)) {
    throw "Create Fly interface data was not found: $interfaces"
}
if (-not (Test-Path -LiteralPath $CreateFlyJar)) {
    throw "Create Fly jar was not found: $CreateFlyJar"
}

$java25Candidates = @()
if ($env:JAVA_HOME) { $java25Candidates += $env:JAVA_HOME }
$java25Candidates += Get-ChildItem -LiteralPath (Join-Path $env:ProgramFiles "Eclipse Adoptium") -Directory -Filter "jdk-25*" -ErrorAction SilentlyContinue |
    Sort-Object Name -Descending |
    ForEach-Object { $_.FullName }
$java25Home = $null
foreach ($candidate in $java25Candidates) {
    $java = Join-Path $candidate "bin\java.exe"
    if ((Test-Path -LiteralPath $java) -and ((& $java -version 2>&1 | Out-String) -match 'version "25\.')) {
        $java25Home = $candidate
        break
    }
}
if (-not $java25Home) {
    throw "Java 25 is required. Install Eclipse Temurin JDK 25 or set JAVA_HOME to it."
}
$env:JAVA_HOME = $java25Home

Write-Host "Running NeoForge 26.3 $Action with $CreateFlyJar" -ForegroundColor Cyan
$gradleTask = switch ($Action) {
    "build" { "build" }
    "client" { "runClient" }
    "server" { "runServer" }
    "gametest" { "runGameTestServer" }
}
Push-Location -LiteralPath $neoForgeProject
try {
    & .\gradlew.bat $gradleTask --console=plain "-Pcreate_jar=$CreateFlyJar" "-Pcreate_interfaces=$interfaces"
    if ($LASTEXITCODE -ne 0) {
        throw "NeoForge 26.3 $Action failed with exit code $LASTEXITCODE."
    }
} finally {
    Pop-Location
}
