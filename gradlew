#!/bin/sh
APP_HOME=$(CDPATH='' cd -- "$(dirname -- "$0")" && pwd)
if [ ! -f "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" ]; then
  echo 'Missing Gradle wrapper JAR. Download the official Gradle 8.9 wrapper JAR from Gradle GitHub,'
  echo 'and place it in gradle/wrapper/gradle-wrapper.jar, or run Setup Gradle Wrapper.bat on Windows.'
  exit 1
fi
exec java -cp "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
