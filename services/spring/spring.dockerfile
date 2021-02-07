FROM openjdk:11-jdk-slim

VOLUME /tmp

ADD /target/backend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]