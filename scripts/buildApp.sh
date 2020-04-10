#!/bin/bash
cd ../angular-app
npm run build
if [ $? -eq 0 ];
then
  echo 'build app OK'
else 
  exit 0 
fi

cd ..
rm -rf src/main/resources/META-INF/resources/app
mkdir src/main/resources/META-INF/resources/app
mv angular-app/dist/* src/main/resources/META-INF/resources/
if [ $? -eq 0 ];
then
	echo "move OK"
else
	exit 0 
fi
