FROM debian:12

RUN apt-get update && apt-get install openjdk-17-jdk nodejs maven npm dos2unix docker-compose haproxy -y ; apt-get install openjdk-17-jdk nodejs maven npm dos2unix docker-compose haproxy -y

COPY haproxy.cfg /etc/haproxy/haproxy.cfg
