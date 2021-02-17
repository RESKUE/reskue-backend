# RESKUE Backend

## General information
- Some commands in this README require a docker-compose version >1.25.4
- All commands described in this README work from the project root directory

## Modes
- Production (prod): All services + Spring Boot in a container
- Development (dev): All services in a container without Spring Boot

## Services
1. Create two files at the project root: `prod.env` and `dev.env`
2. Copy the content from `default.env` into `prod.env` and `dev.env`
3. Fill out all configuration settings. Values that are already set should only be changed if you know what they do.
```
# content-database
CONTENT_DB=content_db   #name of the content database
CONTENT_DB_PORT=5433    #port of the content database
CONTENT_DB_USER=        #username to access the content database
CONTENT_DB_PASSWORD=    #password to access the content database

# auth-database
AUTH_DB=auth_db       #name of the auth database         
AUTH_DB_PORT=5434     #port of the auth database
AUTH_DB_USER=         #username to access the auth database
AUTH_DB_PASSWORD=     #password to access the auth database

# location-database
LOCATION_DB=location_db       #name of the location database
LOCATION_DB_PORT=5435         #port of the location database
LOCATION_DB_USER=             #username to access the location database
LOCATION_DB_PASSWORD=         #password to access the location database

# Postgres
POSTGRES_INITDB_ARGS=--auth-local=password  #local authorization method
POSTGRES_HOST_AUTH_METHOD=password          #host authorization method
DB_VENDOR=POSTGRES                          #database vendor
DB_SCHEMA=public                            #schema that contais all tables

# Keycloak
KEYCLOAK_PORT=5436                #port of the keycloak service
KEYCLOAK_USER=                    #username to access the keycloak service
KEYCLOAK_PASSWORD=                #password to access the keycloak service
KEYCLOAK_REALM=reskue             #keycloak realm
KEYCLOAK_RESOURCE=reskue          #keycloak client
KEYCLOAK_SSL_REQUIRED=external    #is ssl required to connect to keycloak

# RabbitMQ
RABBITMQ_USER=                    #username to access the rabbitmq service
RABBITMQ_PASSWORD=                #password to access the rabbitmq service
RABBITMQ_PORT=5437                #port of the rabbitmq service
RABBITMQ_MANAGEMENT_PORT=15437    #management port of the rabbitmq service

# FROST
LOCATION_PORT=5438                #http port of the frost server
LOCATION_MQTT_PORT=5439           #mqtt port of the frost server

# Spring
SPRING_PORT=8080                                #port of the spring service
LOGGING_LEVEL_ORG_SPRINGFRAMEWORK=WARN          #log levels: ERROR, WARN, INFO, DEBUG or TRACE
SPRING_JPA_HIBERNATE_DDL_AUTO=none              #hibernate auto ddl: validate, update, create, create-drop or none
SPRING_DATASOURCE_INITIALIZATION_MODE=always    #spring data intilization mode
SPRING_DATASOURCE_PLATFORM=postgres             #database vendor
SPRING_DATASOURCE_PLATFORM_JDBC=postgresql      #jdbc database type
KUERES_MEDIA_DIR=                               #local file path to a directory where media will be stored
KUERES_TOPIC_EXCHANGE=reskue-events             #default rabbitmq topic exchange
KUERES_DEFAULT_QUEUE=event-consumer             #default rabbitmq queue
```
4. Start the services in either development or production mode
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
5. Stop all services
  * Stop services in development mode
```
sudo docker-compose -f docker-compose.dev.yml --env-file dev.env down
```
  * Stop services in production mode
```
sudo docker-compose -f docker-compose.prod.yml --env-file prod.env down
```
6. Clean up data directories - omit services that should not be deleted
```
./clean.sh keycloak,rabbitmq,content-db,location-db
```

## Updates
1. Pull the master-branch of reskue-backend
2. Pull the latest docker image of the spring boot application
```
docker login gitlab-ext.iosb.fraunhofer.de:4567
docker pull gitlab-ext.iosb.fraunhofer.de:4567/ilt-pse/ws20_21-mobile-anwendung-zur-unterst-tzung-im-kulturg-terschutz/reskue-backend:latest
```