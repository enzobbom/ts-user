# Basic Docker file
#FROM eclipse-temurin:17-jdk-jammy
#WORKDIR /app
#COPY build/libs/user-0.0.1-SNAPSHOT.jar /app/user.jar
#EXPOSE 8080
#CMD ["java", "-jar", "/app/user.jar"]

# Build
FROM gradle:jdk17 AS build
WORKDIR /app
COPY . .

# Run .jar
RUN gradle build --no-daemon
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/user.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/user.jar"]

