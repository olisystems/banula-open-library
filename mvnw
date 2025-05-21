#!/bin/sh
# Use Java 17 for Maven commands
JAVA_HOME=~/.sdkman/candidates/java/17.0.13-amzn ./mvnw_original "$@"
