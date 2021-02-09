#!/bin/sh

echo "Waiting for services"

responseKeycloak=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://keycloak:8080/auth/realms/reskue)
responseRabbitMQ=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://rabbitmq:15672)
responseFrost=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://frost:8080/FROST-Server/v1.0)

while [ "$responseKeycloak" -ne 200 ] || [ "$responseRabbitMQ" -ne 200 ] || [ "$responseFrost" -ne 200 ]; do

  echo "Not all services started yet"
  echo $responseKeycloak
  echo $responseRabbitMQ
  echo $responseFrost

  sleep 1s

  responseKeycloak=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://keycloak:8080/auth/realms/reskue)
  responseRabbitMQ=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://rabbitmq:15672)
  responseFrost=$(curl --head --write-out '%{http_code}' --silent --output /dev/null http://frost:8080/FROST-Server/v1.0)

done

echo "Services started"
echo $responseKeycloak
echo $responseRabbitMQ
echo $responseFrost

echo "Starting spring"
exec "$@"