FROM openjdk:17-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR de la aplicación al contenedor
COPY target/ParkingFPAAccessManager-2.0.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
