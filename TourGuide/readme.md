# TourGuide

A Spring Boot application that helps users plan their travel by providing nearby attractions, hotel deals, and show tickets. This application enables travelers to discover tourist attractions based on their current location and earn rewards for visiting these attractions.

## 📄 CI/CD Deliverables
All deliverables are generated through the CI/CD pipeline:
* [Docker image](https://hub.docker.com/r/redikan7/tourguide)
* [JaCoCo test coverage report](https://mr-boubakour.github.io/-BOUBAKOUR-MohamedRedha-p8-DistributedSystems-spring/jacoco/)
* [JavaDocs](https://mr-boubakour.github.io/-BOUBAKOUR-MohamedRedha-p8-DistributedSystems-spring/javadocs/)
* Executable JAR (artifact available on GitHub Actions)

## 🚀 Technologies & Stack Used
* **Java 17 / Spring Boot 3.1.1** - core platform
* **Spring Web** - REST API implementation
* **Spring Actuator** - monitoring and metrics
* **Bean Validation** - input validation
* **JUnit Jupiter** - unit and performance testing
* **JaCoCo** - code coverage analysis
* **Maven** - build and dependency management
* **Docker** - containerization
* **GitHub Actions** - CI/CD pipeline

## ⚡ **Performance Improvements Implemented**
* **Location Caching**: Reduced redundant calculations by caching location data.
* **Configuration**: Optimized thread management for better resource control and faster execution. `customTaskExecutor`
* **Use of `CompletableFuture`**: Parallelized the processing of location and reward data for faster execution and improved responsiveness.

## ⚙️ Core Features & Functionality
* User location tracking `Tracker`
* Nearby attraction recommendations (5 closest attractions)
* Reward points calculation for visited attractions
* Trip deals based on user preferences

## 🔗 External Services Integration
* **gpsUtil** - collects user location data from mobile phones or laptops
* **tripPricer** - calculates trip prices and deals
* **RewardsCentral** - gathers reward values for tourist attractions

> Run :
> - mvn install:install-file -Dfile=./libs/gpsUtil.jar -DgroupId=gpsUtil -DartifactId=gpsUtil -Dversion=1.0.0 -Dpackaging=jar
> - mvn install:install-file -Dfile=./libs/RewardCentral.jar -DgroupId=rewardCentral -DartifactId=rewardCentral -Dversion=1.0.0 -Dpackaging=jar
> - mvn install:install-file -Dfile=./libs/TripPricer.jar -DgroupId=tripPricer -DartifactId=tripPricer -Dversion=1.0.0 -Dpackaging=jar

## 🧪 Testing
* TestPerformance - validates system performance with large user volumes
* TestRewardsService - tests reward calculation functionality
* TestTourGuideService - validates core service functionality
* TourguideApplicationTests - application integration tests
