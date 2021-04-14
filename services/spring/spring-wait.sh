#!/bin/sh

echo "Waiting for services"

keycloak_url="${KEYCLOAK_AUTH_SERVER_URL:-http://keycloak:8080/auth}/realms/reskue"
rabbitmq_url="http://${SPRING_RABBITMQ_HOST:-rabbitmq}:15672"
frost_url="http://frost:8080/FROST-Server/"


responseKeycloak=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$keycloak_url")
responseRabbitMQ=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$rabbitmq_url")
responseFrost=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$frost_url")

echo "Waiting for RabbitMQ to get ready at url $rabbitmq_url"
while [ "$responseRabbitMQ" -ne 200 ]; do
  echo "... waiting for RabbitMQ at $rabbitmq_url"
  echo "Response: $responseRabbitMQ"
  sleep 1s
  responseRabbitMQ=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$rabbitmq_url")
done
echo "RabbitMQ is up"

echo "Waiting for FROST to get ready at $frost_url"
while [ "$responseFrost" -ne 200 ]; do
  echo "... waiting for FROST at $frost_url"
  echo "Response: $responseFrost"
  sleep 1s
  responseFrost=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$frost_url")
done
echo "FROST is up"

echo "Waiting for Keycloak to get ready at url $keycloak_url"
while [ "$responseKeycloak" -ne 200 ]; do
  echo "... waiting for Keycloak at $keycloak_url"
  echo "Response: $responseKeycloak"
  sleep 1s
  responseKeycloak=$(curl --head --write-out '%{http_code}' --silent --output /dev/null "$keycloak_url")
done
echo "Keycloak is up"

echo "Services started"
echo "Starting spring"
exec "$@"
