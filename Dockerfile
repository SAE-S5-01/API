FROM openjdk:21-jdk-slim AS build

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY apicliandcollect/ .

COPY docker-config/application.properties apicliandcollect/src/main/resources/application.properties

# Construire l'application avec Maven pour générer le fichier JAR
RUN mvn clean package -DskipTests

# Créer une nouvelle image plus légère avec JDK 21 pour exécuter l'application sans maven
FROM openjdk:21-jdk-slim

# Copier le JAR généré depuis l'étape précédente
COPY --from=build /app/target/apicliandcollect-0.0.1-SNAPSHOT.jar /cliandcollect-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/cliandcollect-api.jar"]