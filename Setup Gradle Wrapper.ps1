# Downloads the official Gradle 8.9 wrapper JAR from the Gradle project's GitHub tag.
$ErrorActionPreference = 'Stop'
$destination = Join-Path $PSScriptRoot 'gradle\wrapper\gradle-wrapper.jar'
$source = 'https://raw.githubusercontent.com/gradle/gradle/v8.9.0/gradle/wrapper/gradle-wrapper.jar'
try {
    if (-not (Test-Path $destination)) {
        Write-Host 'Downloading official Gradle wrapper JAR...'
        Invoke-WebRequest -Uri $source -OutFile $destination -UseBasicParsing
    }
    if ((Get-Item $destination).Length -lt 30000) {
        Remove-Item $destination -Force
        throw 'Wrapper download was incomplete.'
    }
    Add-Type -AssemblyName System.IO.Compression
    Add-Type -AssemblyName System.IO.Compression.FileSystem
    $archive = [System.IO.Compression.ZipFile]::OpenRead($destination)
    $valid = @($archive.Entries | Where-Object { $_.FullName -eq 'org/gradle/wrapper/GradleWrapperMain.class' }).Count -gt 0
    $archive.Dispose()
    if (-not $valid) { Remove-Item $destination -Force; throw 'Downloaded file is not a valid Gradle wrapper JAR.' }
    Write-Host 'Gradle wrapper is ready. You can open the project in Android Studio.'
} catch {
    Write-Error "Could not set up wrapper: $_"
    exit 1
}
