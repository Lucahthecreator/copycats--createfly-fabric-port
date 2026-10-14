@echo off
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\run-dev-client.ps1"
if errorlevel 1 pause
