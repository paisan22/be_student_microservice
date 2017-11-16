FROM openjdk:8-jdk-alpine

MAINTAINER Paisan Rietbroek

COPY target/student-service-0.0.1-SNAPSHOT.jar app.jar

CMD java -jar app.jar