FROM openjdk:24

COPY jasmarty.jar /app/jasmarty.jar
CMD ["java", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "-Xmx128m", "-Dlog4j2.formatMsgNoLookups=True", "-jar", "/app/jasmarty.jar"]
HEALTHCHECK --interval=120s --timeout=10s --retries=3 --start-period=30s CMD curl --user check:check --fail http://localhost:8080/wipf/up || kill 1
