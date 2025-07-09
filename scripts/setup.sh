#!/bin/bash
echo "Setup start"
rm -rf ../target
cd ../angular-app
npm install

# #TODO Fix 360 Viewer
# perl -pi -e 's/false, never>/false >/g' node_modules/@egjs/ngx-view360/lib/ngx-view360.component.d.ts
# perl -pi -e 's/false, never>/false >/g' node_modules/ngx-color-picker/lib/helpers.d.ts
# perl -pi -e 's/false, never>/false >/g' node_modules/ngx-color-picker/lib/color-picker.component.d.ts
# perl -pi -e 's/false, never>/false >/g' node_modules/ngx-color-picker/lib/color-picker.directive.d.ts

# #TODO Fix ngx-photo-editor
perl -pi -e 's/~//g' node_modules/ngx-photo-editor/photo-editor.css

echo "Setup end"

# install ng
# npm install -g @angular/cli

# fix warns:
# npm i -f

# Update npm
# npm install -g npm 
