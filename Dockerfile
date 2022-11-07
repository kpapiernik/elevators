FROM openjdk:17
EXPOSE 8080
ADD target/levators_app.jar elevators_app.jar
ENTRYPOINT ["java", "-jar", "elevators_app.jar"]