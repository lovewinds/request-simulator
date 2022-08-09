# spring-boot-gradle-boilerplate
A boilerplate which uses Spring Boot 2 and Gradle

## Prerequisites
* Gradle 6 +
* JDK 11 +

## Features
This boilerplate comes bundled with the following features:
1. Spring Boot Starter Web (version 2.3.2)
2. [Undertow web server](http://undertow.io/), replacing the default Tomcat server
3. [Spring Actuator](https://spring.io/guides/gs/actuator-service/) for health and metrics
4. [Log4j2](https://logging.apache.org/log4j/2.x/) for better control over logging
5. [Logbook](https://github.com/zalando/logbook) for logging API requests and responses
6. YAML based Spring configuration with support for different deployment environment configuration
7. Different configuration of Log4j2 based on the deployment environment
8. A filter to assign an unique ID for each API request

## How to use?

1. Clone the repository.
```
git clone https://github.com/bipinthite/spring-boot-gradle-boilerplate.git
cd spring-boot-gradle-boilerplate/
```

2. Build the project.
```
./gradlew bootJar
```

3. Run the Spring Boot application.
```
./gradlew bootRun
```

If you need to run the application with different deployment environment properties,
```
SPRING_PROFILES_ACTIVE=production ./gradlew bootRun
```

### Build and run using Docker
1. Build the docker image.
```
docker build \
    --build-arg JAR_FILE=Spring-Boot-Gradle-Boilerplate-0.0.1-SNAPSHOT.jar \
    -t spring-boot-gradle-boilerplate:0.0.1-SNAPSHOT \
    .
```

2. Run the application in docker container.
```
docker run --rm -p 8080:8080 spring-boot-gradle-boilerplate:0.0.1-SNAPSHOT
```
