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

# Copier le script wait-for-it.sh dans l'image
COPY ./docker-config/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

EXPOSE 8080

# Utiliser wait-for-it.sh pour attendre que MySQL soit prêt avant de démarrer l'application
ENTRYPOINT ["/wait-for-it.sh", "cliandcollect-mysql:3306", "--", "java", "-jar", "/cliandcollect-api.jar"]