FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

# Dar permisos de ejecución a gradlew
RUN chmod +x ./gradlew

# Construir el JAR
RUN ./gradlew build --no-daemon

# Usar el JAR generado
COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]