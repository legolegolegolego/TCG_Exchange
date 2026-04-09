FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

# Dar permisos de ejecución a gradlew
RUN chmod +x ./gradlew

# Construir el JAR
RUN ./gradlew build --no-daemon

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "build/libs/TCG_Exchange-*.jar"]