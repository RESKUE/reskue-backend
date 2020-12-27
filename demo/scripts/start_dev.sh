authToken="auth"
reskueToken="reskue"
rabbitmqToken="rabbitmq"

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

docker-compose up --build