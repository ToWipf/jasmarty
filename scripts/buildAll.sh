#!/bin/bash
echo "start build All"

./buildFrontend.sh
if [ $? -eq 0 ]; then
	echo "app OK"
else
	exit 1
fi

./buildBackend.sh
if [ $? -eq 0 ]; then
	echo "mvn OK"
else
	exit 1
fi

echo "end build All"