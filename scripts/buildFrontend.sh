#!/bin/bash
cd ../angular-app
npm run build
if [ $? -eq 0 ]; then
  echo 'build app OK'
else
  exit 1
fi

cd ..
rm -rf src/main/resources/META-INF/resources/app
mkdir src/main/resources/META-INF/resources/app
mv angular-app/dist/app/browser/* src/main/resources/META-INF/resources/app
if [ $? -eq 0 ]; then
  echo "move App OK"
else
  exit 1
fi
cp -r angular-app/public/* src/main/resources/META-INF/resources/app
if [ $? -eq 0 ]; then
  echo "Copy PWA OK"
else
  exit 1
fi
