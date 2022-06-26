#!/bin/bash
cd devproxy
docker-compose up -d

cd ..
cd ..
cd angular-app
npm run startp