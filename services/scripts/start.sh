authToken="auth"
contentToken="content"
rabbitmqToken="rabbitmq"
locationToken="location"

if [[ $1 == *"$authToken"* ]]; then
  echo "deleting auth-db data"
  sudo rm -rf ./auth-db/data/
  mkdir ./auth-db/data
fi

if [[ $1 == *"$contentToken"* ]]; then
  echo "deleting content-db data"
  sudo rm -rf ./content-db/data/
  mkdir ./content-db/data
fi

if [[ $1 == *"$rabbitmqToken"* ]]; then
  echo "deleting rabbitmq data"
  sudo rm -rf ./rabbitmq/data/
  mkdir ./rabbitmq/data
fi

if [[ $1 == *"$locationToken"* ]]; then
  echo "deleting location data"
  sudo rm -rf ./location-db/data/
  mkdir ./location-db/data
fi

sudo docker-compose up --build