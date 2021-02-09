FROM openjdk:11-jdk-slim

ENV ARTIFACT_ID=backend
ENV VERSION=0.0.1-SNAPSHOT

VOLUME /tmp

ADD /target/${ARTIFACT_ID}-${VERSION}.jar app.jar

COPY /services/spring/spring-wait.sh /spring-wait.sh
RUN chmod +x /spring-wait.sh

ENTRYPOINT [ "/spring-wait.sh" ]
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]