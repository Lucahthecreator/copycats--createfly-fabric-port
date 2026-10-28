@echo off
setlocal
title Copycats+ Build and Test

:menu
cls
echo ================================================
echo       Copycats+ Build and Test Menu
echo ================================================
echo Build a selected JAR (saved in dist):
echo   1. Minecraft 1.21.10 beta - Java 21
echo   2. Minecraft 1.21.11 beta - Java 21
echo   3. Minecraft 26.1 release line (26.1.2 target) - Java 25
echo   4. Minecraft 26.2 universal (Create Fly 6.0.9-1 through 6.0.9-2) - Java 25
echo   5. Build all four Fabric JAR targets
echo.
echo Launch a development test client:
echo   6. Minecraft 1.21.10 beta
echo   7. Minecraft 1.21.11 beta
echo   8. Minecraft 26.1.2
echo   9. Minecraft 26.2 with Create Fly 6.0.9-2 beta
echo  10. Minecraft 26.2 compatibility with Create Fly 6.0.9-1
echo   0. Exit
echo.
set "choice="
set /p "choice=Choose an option: "

if "%choice%"=="1" goto build12110
if "%choice%"=="2" goto build12111
if "%choice%"=="3" goto build261
if "%choice%"=="4" goto build262
if "%choice%"=="5" goto buildall
if "%choice%"=="6" goto test12110
if "%choice%"=="7" goto test12111
if "%choice%"=="8" goto test261
if "%choice%"=="9" goto test262beta
if "%choice%"=="10" goto test262stable
if "%choice%"=="0" goto done
echo Invalid selection.
pause
goto menu

:build12110
call :build "1.21.10"
goto menu
:build12111
call :build "1.21.11"
goto menu
:build261
call :build "26.1+"
goto menu
:build262
call :build "26.2"
goto menu
:buildall
call :build "all"
goto menu

:build
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\build-menu.ps1" -Target %~1
if errorlevel 1 echo Build failed. See the Gradle output above.
pause
exit /b

:test12110
call "%~dp0backport-1.21.10\run-test-client.bat"
pause
goto menu
:test12111
call "%~dp0backport-1.21.11\run-test-client.bat"
pause
goto menu
:test261
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\run-dev-client.ps1" -Minecraft 26.1.2
pause
goto menu
:test262beta
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\run-dev-client.ps1" -Minecraft 26.2 -CreateFly beta
pause
goto menu
:test262stable
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\run-dev-client.ps1" -Minecraft 26.2 -CreateFly stable
pause
goto menu
:done
endlocal
