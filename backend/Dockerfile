# ---------- build stage ----------
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app
COPY . .

RUN gradle bootJar --no-daemon

# ---------- runtime stage ----------
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

#COPY build/libs/library-0.0.1-SNAPSHOT.jar app.jar