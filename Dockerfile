FROM openjdk:21-jdk

WORKDIR /app

COPY target/TimeTable-0.0.1-SNAPSHOT.jar app.jar

LABEL authors="sushil"

EXPOSE 8080

CMD ["java","-jar","app.jar"]