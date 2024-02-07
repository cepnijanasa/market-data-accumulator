Market Data Accumulator
========================

Market Data Accumulator is a Spring Boot-based Java 17 application designed to handle market price updates for traded instruments.
Application provides a REST API for vendors to push real-time price updates and empowers downstream clients with access to the latest market prices.
It has in-memory price update cache with automated data cleanup.

## Technology Stack
Spring Boot lets us kick-start an application in minutes and the underlying Spring framework has all the tools necessary to implement, document and test a REST API with ease.
Further evolution of the application would probably include implementing authentication & authorization, persistent storage of data, etc. Spring has the tools to cover these areas.

## Run the App
>mvn spring-boot:run

or

>java -jar market-data-accumulator-0.0.1-SNAPSHOT.jar

## Run the App in Demo Mode
>mvn spring-boot:run -Dspring-boot.run.profiles=demo

or

>java -Dspring.profiles.active=demo -jar market-data-accumulator-0.0.1-SNAPSHOT.jar

