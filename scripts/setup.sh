#!/bin/bash
set -e

echo "Setup start"

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd -- "${SCRIPT_DIR}/.." && pwd)"

rm -rf "${PROJECT_ROOT}/target"
cd "${PROJECT_ROOT}/angular-app"
npm install

# #TODO Fix 360 Viewer
# perl -pi -e 's/false, never>/false >/g' node_modules/@egjs/ngx-view360/lib/ngx-view360.component.d.ts
# perl -pi -e 's/false, never>/false >/g' node_modules/ngx-color-picker/lib/helpers.d.ts
# perl -pi -e 's/false, never>/false >/g' node_modules/ngx-color-picker/lib/color-picker.component.d.ts
# perl -pi -e 's/false, never>/false >/g' node_modules/ngx-color-picker/lib/color-picker.directive.d.ts

# #TODO Fix ngx-photo-editor
if [ -f "node_modules/ngx-photo-editor/photo-editor.css" ]; then
  perl -pi -e 's/~//g' node_modules/ngx-photo-editor/photo-editor.css
fi

echo "Setup end"

# install ng
# npm install -g @angular/cli

# fix warns:
# npm i -f

# Update npm
# npm install -g npm 
