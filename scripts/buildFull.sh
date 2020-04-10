#!/bin/bash
cd ../angular
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
mv angular/dist/* src/main/resources/META-INF/resources/
if [ $? -eq 0 ];
then
	echo "move OK"
else
	exit 0 
fi

mvn package -Dquarkus.package.uber-jar=true
if [ $? -eq 0 ];
then
	echo "build OK"
else
	exit 0
fi
