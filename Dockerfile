FROM openjdk:21-jdk-slim AS build

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY apicliandcollect/ .

# Construire l'application avec Maven pour générer le fichier JAR
RUN mvn clean package -DskipTests

# Créer une nouvelle image plus légère avec JDK 21 pour exécuter l'application sans maven
FROM openjdk:21-jdk-slim

# Copier le JAR généré depuis l'étape précédente
COPY --from=build /app/target/apicliandcollect-0.0.1-SNAPSHOT.jar /cliandcollect-api.jar

EXPOSE 80 8080

# Utiliser la commande par défaut pour démarrer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/cliandcollect-api.jar"]