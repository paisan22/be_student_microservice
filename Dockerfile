FROM openjdk:8-jdk-alpine

MAINTAINER Paisan Rietbroek

ADD target/student-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 4000
ENTRYPOINT ["java","-jar","app.jar"]
