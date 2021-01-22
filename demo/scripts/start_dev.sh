authToken="auth"
reskueToken="reskue"
rabbitmqToken="rabbitmq"
locationToken="location"

if [[ $1 == *"$authToken"* ]]; then
  echo "deleting auth-db data"
  sudo rm -rf ./auth-db/data/
  mkdir ./auth-db/data
fi

if [[ $1 == *"$reskueToken"* ]]; then
  echo "deleting reskue-db data"
  sudo rm -rf ./reskue-db/data/
  mkdir ./reskue-db/data
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

docker-compose up --build