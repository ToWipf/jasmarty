FROM openjdk:17

COPY jasmarty.jar /app/jasmarty.jar
CMD ["java", "-Xmx128m", "-Xmx128m", "-jar", "/app/jasmarty.jar", "–Dlog4j2.formatMsgNoLookups=True"]
HEALTHCHECK --interval=120s --start-period=30s CMD curl --fail http://localhost:8080/wipf/up || kill 1
