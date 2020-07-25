#!/bin/bash
cp ../target/jasmarty-1.0-SNAPSHOT-runner.jar jasmarty.jar
docker build -t jasmarty .


docker tag jasmarty docker.pkg.github.com/towipf/jasmarty/jasmarty:0.75
docker push docker.pkg.github.com/towipf/jasmarty/jasmarty:0.75


#--platform linux/arm/v7 

docker buildx build --platform linux/arm -t docker.pkg.github.com/towipf/jasmarty/jasmarty:0.75arm -f Dockerfile.arm .
docker push docker.pkg.github.com/towipf/jasmarty/jasmarty:0.75arm

rm jasmarty.jar