#!/bin/bash
cd ..
mvn package
if [ $? -eq 0 ]; then
	echo "mvn OK"
else
	exit 1
fi
