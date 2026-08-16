#!/bin/bash
set -e

echo "start build All"

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"

"${SCRIPT_DIR}/buildFrontend.sh"
echo "app OK"

"${SCRIPT_DIR}/buildBackend.sh"
echo "mvn OK"

echo "end build All"