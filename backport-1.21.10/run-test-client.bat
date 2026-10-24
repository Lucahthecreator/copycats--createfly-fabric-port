@echo off
setlocal
set "JAVA_HOME="
for /d %%J in ("%ProgramFiles%\Eclipse Adoptium\jdk-21*") do if exist "%%~fJ\bin\java.exe" set "JAVA_HOME=%%~fJ"
if not defined JAVA_HOME (
    echo Java 21 was not found. Install Eclipse Temurin JDK 21 before launching this client.
    pause
    exit /b 1
)
pushd "%~dp0.."
call gradlew.bat -p backport-1.21.10 runClient --console=plain
if errorlevel 1 (
    echo.
    echo The test client stopped with an error. Check backport-1.21.10\run-1.21.10\crash-reports and logs\latest.log.
    pause
)
popd
