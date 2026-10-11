FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
ARG MODULE
COPY . .
RUN mvn -B -pl "services/$MODULE" -am -DskipTests package && cp "services/$MODULE/target/$MODULE-0.0.1-SNAPSHOT.jar" /application.jar

FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl && addgroup -S taxi && adduser -S taxi -G taxi
WORKDIR /app
RUN mkdir -p /app/data && chown taxi:taxi /app/data
COPY --from=build /application.jar /app/application.jar
USER taxi
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-jar", "/app/application.jar"]
