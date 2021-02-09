# RESKUE Backend

## General information
- Some commands in this README require a docker-compose version >1.25.4

## Using the services
1. Copy default.env to prod.env and dev.env
2. Fill out prod.env and dev.env for production and development mode respectively
3. Start the services in either development or production mode
  * Start services in development mode
```
sudo docker-compose -f docker-compose.dev.yml --env-file dev.env up --build
```
  * Start services in production mode
```
docker login gitlab-ext.iosb.fraunhofer.de:4567
docker pull gitlab-ext.iosb.fraunhofer.de:4567/ilt-pse/ws20_21-mobile-anwendung-zur-unterst-tzung-im-kulturg-terschutz/reskue-backend:latest
sudo docker-compose -f docker-compose.prod.yml --env-file prod.env up -d
```
4. Stop all services
  * Stop services in development mode
```
sudo docker-compose -f docker-compose.dev.yml --env-file dev.env down
```
  * Stop services in production mode
```
sudo docker-compose -f docker-compose.prod.yml --env-file prod.env down
```
5. Clean up data directories - omit services that should not be deleted
```
./clean.sh keycloak,rabbitmq,content-db,location-db
```

## Updating reskue-backend version
1. Update version in all .env files: VERSION
2. Update version in .gitlab-ci.yml: VERSION