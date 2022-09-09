#!/bin/bash
cd devproxy
docker-compose down
docker-compose up -d
if [ $? -eq 0 ]; then
    echo "docker OK"
    cd ..
    cd ..
    cd angular-app
    npm run startp
else
    echo "docker FAIL"
    exit 1
fi
