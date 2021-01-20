FROM postgis/postgis:11-2.5-alpine

COPY ./init/ /docker-entrypoint-initdb.d/