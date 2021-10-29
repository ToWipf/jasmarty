FROM openjdk

COPY jasmarty.jar /app/jasmarty.jar
CMD ["java","-jar","/app/jasmarty.jar"]
HEALTHCHECK --interval=120s --start-period=30s CMD curl --fail http://localhost:8080/wipf/up || kill 1
