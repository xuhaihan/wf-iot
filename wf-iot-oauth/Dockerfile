FROM openjdk:8-jre
MAINTAINER zhsyourai <mysoftzhs@gmail.com>
VOLUME /tmp
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.profiles.active=docker

ARG JAR_FILE
ADD ${JAR_FILE} /app.jar
RUN sh -c 'touch /app.jar'