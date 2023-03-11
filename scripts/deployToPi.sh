#!/bin/bash
JASMARTY_SERVER_SSH=pi@192.168.2.11
IMAGE_ID=jasmarty
VERSION=$(date '+%Y-%m-%d')

echo Name= $IMAGE_ID:${VERSION}

./buildAll.sh
if [ $? -eq 0 ]; then
	echo "build OK"
else
	exit 1
fi

pscp ../target/jasmarty-1.0-SNAPSHOT-runner.jar wipf@192.168.2.11:/home/wipf/jasmarty.jar
pscp Dockerfile.arm_64 wipf@192.168.2.11:/home/wipf/Dockerfile

plink -batch -ssh pi@192.168.2.11 "cd /home/wipf/ && sudo docker build . -t $IMAGE_ID:${VERSION}"
