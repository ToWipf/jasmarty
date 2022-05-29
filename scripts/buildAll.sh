#!/bin/bash
echo "buildFull"

./buildFrontend.sh
if [ $? -eq 0 ];
then
	echo "app OK"
else
	exit 1
fi

./buildBackend.sh
if [ $? -eq 0 ];
then
	echo "mvn OK"
else
	exit 1
fi
