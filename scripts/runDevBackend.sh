#!/bin/bash
export JAVA_TOOL_OPTIONS="--add-opens=java.base/java.lang=ALL-UNNAMED"
mvn compile quarkus:dev -f ../
