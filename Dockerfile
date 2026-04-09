FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

# Construir el JAR
RUN ./gradlew build --no-daemon

# Usar el JAR generado
COPY build/libs/*.jar app.jar

# Puerto
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]