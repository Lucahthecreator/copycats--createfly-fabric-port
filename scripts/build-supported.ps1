$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$dist = Join-Path $root "dist"
$targets = @("26.1.2", "26.2")

New-Item -ItemType Directory -Path $dist -Force | Out-Null

foreach ($target in $targets) {
    & (Join-Path $root "gradlew.bat") clean build "-Ptarget_version=$target"
    if ($LASTEXITCODE -ne 0) {
        throw "Build failed for target $target"
    }

    $jar = Get-ChildItem -LiteralPath (Join-Path $root "build\libs") -Filter "*.jar" |
        Where-Object { $_.Name -notlike "*-sources.jar" } |
        Select-Object -First 1

    if ($null -eq $jar) {
        throw "No release jar was produced for target $target"
    }

    Copy-Item -LiteralPath $jar.FullName -Destination (Join-Path $dist $jar.Name) -Force
}

Get-ChildItem -LiteralPath $dist -Filter "*.jar" | Select-Object FullName, Length, LastWriteTime
