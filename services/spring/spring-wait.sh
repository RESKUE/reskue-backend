#!/bin/sh

echo "Waiting for services"

keycloak_url="${KEYCLOAK_AUTH_SERVER_URL:-http://keycloak:8080/auth}/realms/reskue"
rabbitmq_url="http://${SPRING_RABBITMQ_HOST:-rabbitmq:}:${SPRING_RABBITMQ_HTTP_PORT:-15672}"
frost_url="${KUERES_FROST_URL:-http://frost:8080/FROST-Server/}"


responseKeycloak=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$keycloak_url")
responseRabbitMQ=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$rabbitmq_url")
responseFrost=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$frost_url")

echo "Waiting for RabbitMQ to get ready at url $rabbitmq_url"
while [ "$responseRabbitMQ" -ne 200 ]; do
  echo "."
  sleep 1s
  responseRabbitMQ=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$rabbitmq_url")
done

echo "Waiting for FROST to get ready at $frost_url"
while [ "$responseFrost" -ne 200 ]; do
  echo "."
  echo $responseFrost
  sleep 1s
  responseFrost=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$frost_url")
done

echo "Waiting for Keycloak to get ready at url $keycloak_url"
while [ "$responseKeycloak" -ne 200 ]; do
  echo "."
  sleep 1s
  responseKeycloak=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$keycloak_url")
done

echo "Services started"
echo $responseKeycloak
echo $responseRabbitMQ
echo $responseFrost

echo "Starting spring"
exec "$@"