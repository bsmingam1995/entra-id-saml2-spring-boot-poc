FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copia primero los archivos de Gradle
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Fuerza permisos y formato LF para gradlew
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Construye la app
RUN ./gradlew build --no-daemon

# Copia el código fuente y construye de nuevo (opcional)
COPY src ./src
RUN ./gradlew build -x test --no-daemon

# Fase final
FROM eclipse-temurin:17-jre
WORKDIR /app
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]