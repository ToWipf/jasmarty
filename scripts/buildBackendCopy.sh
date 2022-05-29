#!/bin/bash

./buildBackend.sh
if [ $? -eq 0 ]; then
	echo "mvn OK"
else
	exit 1
fi

./copy.sh
if [ $? -eq 0 ]; then
	echo "copy OK"
else
	exit 1
fi
