#!/bin/sh

echo "Waiting for services"

keycloak_url="${KEYCLOAK_AUTH_SERVER_URL:-http://keycloak:8080/auth}/realms/reskue"
rabbitmq_url="http://${SPRING_RABBITMQ_HOST:-rabbitmq}:${SPRING_RABBITMQ_PORT:-15672}"
frost_url="${KUERES_FROST_URL:-http://frost:8080/FROST-Server/}"

while [ "$responseKeycloak" -ne 200 ] || [ "$responseRabbitMQ" -ne 200 ] || [ "$responseFrost" -ne 200 ]; do

  echo "Not all services started yet"
  echo "Keycloak status: $responseKeycloak"
  echo "RabbitMQ status: $responseRabbitMQ"
  echo "Location statue: $responseFrost"

  sleep 1s

  responseKeycloak=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://keycloak:${KEYCLOAK_WAIT_PORT}/auth/realms/reskue)
  responseRabbitMQ=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://rabbitmq:${RABBITMQ_WAIT_PORT})
  responseFrost=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://frost:${LOCATION_WAIT_PORT}/FROST-Server/v1.0)

done

echo "Services started"
echo $responseKeycloak
echo $responseRabbitMQ
echo $responseFrost

echo "Starting spring"
exec "$@"
