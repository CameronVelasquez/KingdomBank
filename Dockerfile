# Define la imagen base
FROM gradle:7.5.1-jdk11-alpine

# Copia el archivo JAR de la aplicación en el contenedor
COPY . .

#build
RUN gradle build

# Expone el puerto 8080 para acceder a la aplicación
EXPOSE 8080

# Define el comando para ejecutar la aplicación al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "build/libs/HomeBanking-0.0.1-SNAPSHOT.jar"]