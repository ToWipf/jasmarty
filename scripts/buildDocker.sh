#!/bin/bash
cp ../target/jasmarty-1.0-SNAPSHOT-runner.jar jasmarty.jar
docker build -t jasmarty .
rm jasmarty.jar

docker tag jasmarty docker.pkg.github.com/towipf/jasmarty/jasmarty:0.75
docker push docker.pkg.github.com/towipf/jasmarty/jasmarty:0.75


#--platform linux/arm/v7 