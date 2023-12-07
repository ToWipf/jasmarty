#/bin/bash

docker build -t wipfdebug .

#docker run -p 8080:8080 -v $(pwd)/:/wipf -v $(pwd)/.cache/.m2:/root/.m2 -v $(pwd)/.cache/.npm:/root/.npm -it wipfdebug bash

#PS
cd ..\..
docker run -p 8080:8080 -v ${PWD}/:/wipf -v ${PWD}/.cache/.m2:/root/.m2 -v ${PWD}/.cache/.npm:/root/.npm -it wipfdebug bash


#docker run -p 8080:8080 -v ($pwd.Path)/:/wipf -it wipfdebug bash
#-v $home\.m2:/root/.m2
#($home)\.npm/:root/.npm