#!/bin/bash
cd ..
mvn package -Dquarkus.package.uber-jar=true
if [ $? -eq 0 ];
then
	echo "mvn OK"
else
	exit 1
fi