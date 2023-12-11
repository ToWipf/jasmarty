# Build

```sh
docker build -t wipfdebug .
```

## PS

```powershell
cd ..\..
docker run -p 7777:7777 -v ${PWD}/:/wipf -v ${PWD}/.cache/.m2:/root/.m2 -v ${PWD}/.cache/.npm:/root/.npm -it wipfdebug bash
```

service haproxy start
cd /wipf/angular-app
npm run startp &
cd ..
mvn compile quarkus:dev &
