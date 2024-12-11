#!/usr/bin/env bash

WAITFORIT_HOST=$1
WAITFORIT_PORT=$2
WAITFORIT_TIMEOUT=30  # Timeout de 30 secondes (à ajuster si nécessaire)

echo "Attente de MySQL pour être prêt sur $WAITFORIT_HOST:$WAITFORIT_PORT"

# Attente initiale de 10 secondes pour s'assurer que les services DNS sont prêts
sleep 10

for i in $(seq 1 $WAITFORIT_TIMEOUT); do
  if nc -z "$WAITFORIT_HOST" "$WAITFORIT_PORT"; then
    echo "$WAITFORIT_HOST:$WAITFORIT_PORT est prêt!"
    break
  fi
  echo "Attente ($i/$WAITFORIT_TIMEOUT)..."
  sleep 1
done

if ! nc -z "$WAITFORIT_HOST" "$WAITFORIT_PORT"; then
  echo "Timeout: MySQL n'est pas prêt après $WAITFORIT_TIMEOUT secondes"
  exit 1
fi

# Si MySQL est prêt, démarrer l'application.
echo "Démarrage de l'application Spring Boot..."
exec java -jar /cliandcollect-api.jar