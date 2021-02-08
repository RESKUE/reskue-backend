FROM openjdk:11-jdk-slim

ENV ARTIFACT_ID=backend
ENV VERSION=0.0.1-SNAPSHOT

VOLUME /tmp

ADD /target/${ARTIFACT_ID}-${VERSION}.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]