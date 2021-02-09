#!/bin/sh

# Wait for:
# - keycloak: curl http://localhost:5436/auth/realms/reskue
# - rabbitmq: curl http://localhost:15437
# - frost: curl http://localhost:5438/FROST-Server/v1.0
#
# Restart services manually:
#   sudo docker-compose -f docker-compose.dev.yml --env-file dev.env restart keycloak rabbitmq frost

echo "Waiting for services"

responseKeycloak=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://localhost:5436/auth/realms/reskue)
responseRabbitMQ=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://localhost:15437)
responseFrost=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://localhost:5438/FROST-Server/v1.0)

while [ "$responseKeycloak" -ne 200 ] || [ "$responseRabbitMQ" -ne 200 ] || [ "$responseFrost" -ne 200 ]; do

  echo "Not all services started yet"
  echo $responseKeycloak
  echo $responseRabbitMQ
  echo $responseFrost

  sleep 1s

  responseKeycloak=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://localhost:5436/auth/realms/reskue)
  responseRabbitMQ=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://localhost:15437)
  responseFrost=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://localhost:5438/FROST-Server/v1.0)

done

echo "Services started"
echo $responseKeycloak
echo $responseRabbitMQ
echo $responseFrost

echo "Starting spring"
exec "$@"