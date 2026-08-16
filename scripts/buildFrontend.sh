#!/bin/bash
set -e

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd -- "${SCRIPT_DIR}/.." && pwd)"

cd "${PROJECT_ROOT}/angular-app"
npm run build

echo 'build app OK'

cd "${PROJECT_ROOT}"
rm -rf src/main/resources/META-INF/resources/app
mkdir -p src/main/resources/META-INF/resources/app
mv angular-app/dist/app/browser/* src/main/resources/META-INF/resources/app

echo "move App OK"

cp -r angular-app/public/* src/main/resources/META-INF/resources/app

echo "Copy PWA OK"
