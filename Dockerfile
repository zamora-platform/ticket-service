# syntax=docker/dockerfile:1
FROM gradle:7.6.1-jdk17-alpine AS builder
WORKDIR /app
COPY build.gradle.kts .
COPY gradle.properties .
COPY ./library ./library
COPY settings.gradle.kts.build settings.gradle.kts
RUN echo 'include("apps:ticket-service")' >> settings.gradle.kts
COPY ./apps/ticket-service ./apps/ticket-service
RUN --mount=type=cache,id=gradle,target=/root/.gradle \
    --mount=type=cache,id=gradle,target=/home/gradle/.gradle \
    gradle :apps:ticket-service:bootJar --no-daemon

FROM openjdk:17-alpine
EXPOSE 8113
WORKDIR /app
COPY --from=builder /app/apps/ticket-service/build/libs/*.jar ticket-service.jar
ENTRYPOINT ["java", "-jar" ,"ticket-service.jar"]
