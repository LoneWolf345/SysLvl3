#!/bin/sh

# Lightweight Gradle wrapper shim that relies on the host Gradle installation.
# The project avoids committing the binary gradle-wrapper.jar, so this script
# delegates to the installed `gradle` executable while preserving a familiar
# entry point for local development and CI.

set -eu

if ! command -v gradle >/dev/null 2>&1; then
  echo "Gradle executable not found on PATH. Please install Gradle 8.0+." >&2
  exit 1
fi

exec gradle "$@"
