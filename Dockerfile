FROM gradle:8.10.2-jdk21-alpine AS base
WORKDIR /app
COPY . .
RUN ["/bin/chmod", "a+rwx", "./gradlew"]
RUN ["gradle", "--no-daemon", ":server:build"]

ARG serverVersion=2.1.1
RUN ["/bin/sh", "-c", "mv ./server/build/libs/server-$serverVersion-all.jar /server.jar"]

FROM alpine:latest
RUN apk add --no-cache openjdk21-jre

WORKDIR /app
COPY --from=base server.jar server.jar

EXPOSE 6667
ENTRYPOINT ["java", "-jar", "server.jar"]
