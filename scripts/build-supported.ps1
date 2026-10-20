$ErrorActionPreference = "Stop"
$buildMenu = Join-Path $PSScriptRoot "build-menu.ps1"
& $buildMenu -Target all
if ($LASTEXITCODE -ne 0) {
    throw "One or more supported builds failed."
}
