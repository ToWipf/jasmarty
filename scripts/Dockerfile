FROM openjdk:24

COPY jasmarty.jar /app/jasmarty.jar
CMD ["java", "-Xmx128m", "-Xmx128m", "-jar", "/app/jasmarty.jar", "–Dlog4j2.formatMsgNoLookups=True"]
HEALTHCHECK --interval=120s --timeout=10s --retries=3 --start-period=30s CMD curl --user check:check --fail http://localhost:8080/wipf/up || kill 1
