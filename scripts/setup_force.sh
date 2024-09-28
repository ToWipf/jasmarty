#!/bin/bash
echo "Setup Force start"
rm -rf ../target
rm -rf ../angular-app/node_modules
rm -rf ../angular-app/.angular/cache
cd ../angular-app
npm install

# #TODO Fix 360 Viewer
# perl -pi -e 's/false, never>/false >/g' node_modules/@egjs/ngx-view360/lib/ngx-view360.component.d.ts
# perl -pi -e 's/false, never>/false >/g' node_modules/ngx-color-picker/lib/helpers.d.ts
# perl -pi -e 's/false, never>/false >/g' node_modules/ngx-color-picker/lib/color-picker.component.d.ts
# perl -pi -e 's/false, never>/false >/g' node_modules/ngx-color-picker/lib/color-picker.directive.d.ts

echo "Setup end"

# install ng
# npm install -g @angular/cli

# fix warns:
# npm i -f

# Update npm
# npm install -g npm 
