# Construct base build environment and load source code.
FROM openjdk:8-jdk AS APP_BUILD_IMAGE
COPY ./ /app

# Build source code.
RUN cd /app && ./gradlew clean build -xtest -xtodolist-frontend:build && chmod +x /app/**/build/libs/*.jar

# Construct base runtime environment
FROM gcr.io/distroless/java:11
COPY --from=APP_BUILD_IMAGE /app/**/build/libs/*.jar /app/