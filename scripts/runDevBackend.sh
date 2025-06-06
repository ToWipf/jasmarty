#!/bin/bash
export JAVA_TOOL_OPTIONS="--enable-native-access=ALL-UNNAMED"
mvn compile quarkus:dev -f ../
