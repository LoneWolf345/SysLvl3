@ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION

where gradle >NUL 2>&1
IF NOT %ERRORLEVEL% == 0 (
  ECHO Gradle executable not found on PATH. Please install Gradle 8.0+.
  EXIT /B 1
)

gradle %*
