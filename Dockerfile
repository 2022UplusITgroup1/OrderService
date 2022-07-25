FROM openjdk:11
ARG JAR_FILE=build/libs/orderservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} orderservice.jar
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "/orderservice.jar"]
