# Elevators Simulation
***

The project shows a simplified simulation of an elevator management system in a building in the form of a REST API.

## Technologies
***
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Postgres SQL
- Lombok
- JUnit 5
- Mockito
- AssertJ
- Docker

## Requirements
***
- Docker installed - [link](https://www.docker.com/products/docker-desktop/)
- Maven installed - [link](https://maven.apache.org/download.cgi)
- Java installed - [link](https://jdk.java.net/archive/)

## How to run application
***
First you need to download this repository to your local machine.\
Next open terminal in the project main directory and use command:
- `mvn clean install` if you want to compile code and run unit tests
- `mvn clean install -DskipTests` if you want to compile code without running unit tests\

Next make sure that your Docker app is running. Then use this command in terminal:
`docker-compose up -d --build`\
To check application logs use: `docker-compose logs -f`\
To stop application use: `docker-compose stop`


## REST API Documentation
***
You can find the documentation at the [link](https://documenter.getpostman.com/view/6405483/2s8YekQudJ)
![](https://user-images.githubusercontent.com/30383691/200979762-66cf31a9-ea69-42cc-9bf4-eae3462b0d4b.png)

Additionally, in the `src / main / resources` folder you can find the file Elevators.postman_collection.json with which you can import the collection for testing into your Postman.