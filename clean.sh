keycloakToken="keycloak"
contentToken="content-db"
rabbitmqToken="rabbitmq"
locationToken="location-db"

if [[ $1 == *"$keycloakToken"* ]]; then
  echo "deleting keycloak data"
  sudo rm -rf ./services/keycloak-db/data/
  mkdir ./services/keycloak-db/data
fi

if [[ $1 == *"$contentToken"* ]]; then
  echo "deleting content-db data"
  sudo rm -rf ./services/content-db/data/
  mkdir ./services/content-db/data
fi

if [[ $1 == *"$rabbitmqToken"* ]]; then
  echo "deleting rabbitmq data"
  sudo rm -rf ./services/rabbitmq/data/
  mkdir ./services/rabbitmq/data
fi

if [[ $1 == *"$locationToken"* ]]; then
  echo "deleting location-db data"
  sudo rm -rf ./services/location-db/data/
  mkdir ./services/location-db/data
fi