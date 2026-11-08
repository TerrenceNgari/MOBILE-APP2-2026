@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0Setup Gradle Wrapper.ps1"
if errorlevel 1 (
  echo Setup failed. Check your internet connection or the README.
) else (
  echo Wrapper setup completed.
)
pause
