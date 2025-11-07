FROM openjdk:26-jdk-slim

# Non-root user einrichten
RUN addgroup --system app && adduser --system --ingroup app app
USER app

# App kopieren
WORKDIR /app
COPY jasmarty.jar /app/jasmarty.jar

# Startkommando
CMD ["java", "--enable-native-access=ALL-UNNAMED", "-Xmx128m", "-jar", "/app/jasmarty.jar"]

# Healthcheck
HEALTHCHECK --interval=120s --timeout=10s --retries=3 --start-period=30s \
    CMD curl --user check:check --fail http://localhost:8080/wipf/up || kill 1
