# RESKUE Backend

## Needed before getting started
- npm
- docker and docker-compose
- git

## Modes
- Production (prod): All services + Spring Boot in a container
- Development (dev): All services in a container without Spring Boot

## Project structure
```
./reskue-backend/
|-- docs    JavaDoc documentation
|-- services  All backend services
|-- src
|   |-- main  Java source files
|   `-- test  Java test source files
`-- test-coverage   Test coverage report
```

## Documentation
- JavaDoc documentation: Open docs/index.html as a website.
- OpenAPI documentation: http://SPRING_ADDRESS/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/

## Getting started - read before doing anything
1. Clone the master branch for use in production mode
```
git clone --single-branch --branch master https://gitlab-ext.iosb.fraunhofer.de/ilt-pse/ws20_21-mobile-anwendung-zur-unterst-tzung-im-kulturg-terschutz/reskue-backend.git
```
or the develop branch for use in development mode
```
git clone --single-branch --branch develop https://gitlab-ext.iosb.fraunhofer.de/ilt-pse/ws20_21-mobile-anwendung-zur-unterst-tzung-im-kulturg-terschutz/reskue-backend.git
```
2. Change into the repository directory
```
cd reskue-backend
```
3. Install npm dependencies
```
npm install
```
4. Update the project
```
npm run update
```
> NOTE: You have to be logged into the Fraunhofers GitLab Docker Registry for this.
> This can be done with:
> ```
> docker login gitlab-ext.iosb.fraunhofer.de:4567
> ```

5. Create enviroment files for development and/or production mode
```
Production mode:
cp default.env prod.env

Development mode:
cp default.env dev.env
```
6. Fill out all missing entries in the enviroment files. An overview over all enviroment variables can be found at the end of this README.

## Running the project
### Production mode
- Starting the backend
```
With console output:
npm run start:prod

Without console output:
npm run start:prod:d
```
- Stopping the backend
```
npm run stop:prod
```

### Development mode
- Starting the backend
```
npm run start:dev
```
- Stopping the backend
```
npm run stop:dev
```
- Running the Postman API tests with Newman. All backend services have to be running for this.
```
npm run test:newman
```
- Run API tests and generate test coverage. All backend services have to be running for this.
```
npm run test
```
- Generate JavaDocs.
```
npm run docs
```

## Update
Update the project - also updates the current git branch
```
npm run update
```

### Cleanup
- Clean Keycloak data
```
npm run clean:keycloak
```
- Clean the content database
```
npm run clean:content
```
- Clean RabbitMQ data
```
npm run clean:rabbitmq
```
- Clean the location database
```
npm run clean:location
```
- Clean all data directories. Warning: also deletes Keycloak user data!
```
npm run clean:all
```

## Enviroment variables
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
#log levels: ERROR, WARN, INFO, DEBUG or TRACE
LOGGING_LEVEL_ORG_SPRINGFRAMEWORK=WARN          
SPRING_JPA_HIBERNATE_DDL_AUTO=none              #hibernate auto ddl: validate, update, create, create-drop or none
SPRING_DATASOURCE_INITIALIZATION_MODE=always    #spring data intilization mode
SPRING_DATASOURCE_PLATFORM=postgres             #database vendor
SPRING_DATASOURCE_PLATFORM_JDBC=postgresql      #jdbc database type
#local file path to a directory where media will be stored
KUERES_MEDIA_DIR=                               
#url to a Nominatim server used for the location service
KUERES_NOMINATIM_URL=https://nominatim.openstreetmap.org  
```