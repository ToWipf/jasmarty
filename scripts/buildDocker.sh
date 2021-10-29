#!/bin/bash
cp ../target/jasmarty-1.0-SNAPSHOT-runner.jar jasmarty.jar

docker buildx create --name mybuilder
docker buildx use mybuilder

#docker build -t jasmarty .
#docker tag jasmarty docker.pkg.github.com/towipf/jasmarty/jasmarty:0.80
#docker push docker.pkg.github.com/towipf/jasmarty/jasmarty:0.80

# platform linux/arm/v7 

docker buildx build --platform linux/arm -t jatest -f Dockerfile.arm .
#docker buildx build --platform linux/arm -t docker.pkg.github.com/towipf/jasmarty/jasmarty:0.80-1arm -f Dockerfile.arm .
#docker push docker.pkg.github.com/towipf/jasmarty/jasmarty:0.80-1arm 

rm jasmarty.jar