@echo off
setlocal
set "APP_HOME=%~dp0"
if not exist "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" (
    powershell -NoProfile -ExecutionPolicy Bypass -File "%APP_HOME%Setup Gradle Wrapper.ps1"
    if errorlevel 1 exit /b 1
)
java -cp "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
exit /b %ERRORLEVEL%
