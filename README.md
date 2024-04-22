<div style="text-align:center;">
    <img src="OIG2.QcRp9W8mofV14U1W5d4_.jpeg" width="300">
</div>
<div style="text-align:center;">
    
# Vessel Keeper Application
  Welcome to the Marine Vessel Keeper application! This document provides guidance on how to use the various features 
  and functionalities offered by the application.
</div>

## Table of Contents

1. [Introduction](#introduction)
2. [Technologies used](#technologies_used)
3. [Getting Started](#getting-started)
4. [Authentication Service](#authentication-service)
5. [Gateway Service](#gate-service)
6. [Vessel Service](#vessel-service)
7. [Voyage Service](#voyage-service)
8. [Crew Service](#crew-service)

## Introduction <a name="introduction"></a>

The Marine Vessel Keeper application is designed to manage information about vessels, voyages, seamen,
and users. It provides features for creating, updating, and deleting records related to these entities.

## Technologies used <a name="technologies_used"></a>

- Language - Java 17
- Framework - Spring Boot 3
- Database - PostgreSQL
- Working with DB - Hibernate, JPA, Spring Data
- Data migration - Liquibase
- Security - Spring Security
- Authentication - Basic auth. / JWT token
- Password Encoder - BCryptPasswordEncoder
- Scheduling - Cron
- Code documentation - JavaDoc, Swagger
- Mapping - MapStruct
- Exception handling - Controller advice
- Testing - Junit, Mockito, Data JPA test, WebMVC test
- Build System - Maven
- Containerization - Docker/DockerHub, Docker compose
- Third party API connection - Feign
- Message brocker - RabbitMQ
- Routing - Spring Cloud Gateway 
- Deploy - AWS Elastic Server
- Version control - Git/GitHub

## Getting Started <a name="getting-started"></a>

To begin using the Marine Vessel Keeper application, follow these steps:

1. Install Docker (If necessary).
2. Clone the repository from [https://github.com/wtypmah24/Vessel_keeper_microservices.git].
3. Add an application-secrets.properties file with the following keys and their respective values:
4. "secret_key" for the Vessel API, "vessel_finder_api_key" for the Vessel Finder API,
5. and "google_api" for the Google Static Map API.
4. Navigate to the project directory.
5. Run docker-compose up command.
6. To check all end points visit [/swagger-ui.html]

If you want to run it on a server:

1. Clone the repository from [https://github.com/wtypmah24/Vessel_keeper_microservices.git].
2. Add an application-secrets.properties file with the following keys and their respective values:
3. "secret_key" for the Vessel API, "vessel_finder_api_key" for the Vessel Finder API, and "google_api"
4. for the Google Static Map API.
3. Run the [bash script](https://github.com/wtypmah24/Vessel_keeper/blob/main/install_dependencies.sh) which will
   download necessary programs such as Git, Java, Maven, Docker, Docker Compose onto the server.
4. Navigate to the project directory.
5. Run docker-compose up command.
6. To check all end points visit [/swagger-ui.html]

## Authentication Service <a name="authentication-service"></a>

The User Management module allows administrators to manage user accounts within the system.
Users can have different roles such as OWNER, OPERATIONAL_MANAGER, CREW_MANAGER.

- [UserService](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/service/user/UserService.java):
  Responsible for user registration, creation, deletion, and retrieval.
- [UserRepository](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/repository/UserRepository.java):
  Data access layer for managing user entities.
- [UserMapper](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/mapper/UserMapper.java):
  Handles mapping between user DTOs and entities.

## Gateway Service <a name="gate-service"></a> 

-
-
-

## Vessel Service <a name="vessel-service"></a>

The Vessel Management module enables users to manage vessel-related information, including vessel
types and crew members.

- [VesselService](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/service/vessel/VesselService.java):
  Manages vessel creation, deletion, and retrieval.
- [VesselRepository](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/repository/VesselRepository.java):
  Provides data access methods for vessel entities.
- [VesselMapper](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/mapper/VesselMapper.java):
  Handles mapping between vessel DTOs and entities.
- [PositionService](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/service/vessel/PositionService.java):
  Manages functionality to generate a static Google Maps URL with markers
- for the positions of vessels.
- [VesselFinderService](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/service/vessel/VesselFinderService.java):
  Provides functionality to retrieve real-time vessel information from a remote service.

## Voyage Service <a name="voyage-service"></a>

The Voyage Management module facilitates the management of voyage details such as ports of loading and discharging,
start and end dates, and vessel assignments.

- [VoyageService](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/service/voyage/VoyageService.java):
  Manages voyage creation, deletion, and retrieval.
- [VoyageRepository](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/repository/VoyageRepository.java):
  Provides data access methods for voyage entities.
- [VoyageMapper](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/mapper/VoyageMapper.java):
  Handles mapping between voyage DTOs and entities.

## Crew Service <a name="crew-service"></a>

The Seaman Management module allows users to manage seaman-related information, including certificates
and service records.

- [SeamanService](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/service/seaman/SeamanService.java):
  Manages seaman creation, deletion, and retrieval.
- [SeamanRepository](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/repository/SeamanRepository.java):
  Provides data access methods for seaman entities.
- [SeamanMapper](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/mapper/SeamanMapper.java):
  Handles mapping between seaman DTOs and entities.
- [RecordOfServiceService](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/service/seaman/RecordOfServiceService.java):
  Manages seamen's record of services.
- [RecordOfServiceRepository](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/repository/RecordOfServiceRepository.java):
  Provides data access methods for record of services.
- [RecordOfServiceMapper](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/mapper/RecordOfServiceMapper.java):
  Handles mapping between record of services dto and entity.
- [CertificateService](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/service/seaman/CertificateService.java):
  Manages seamen's certificate adding and deleting.
- [CertificateRepository](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/repository/CertificateRepository.java):
  Provides data access methods for seamen's certificates entities.
- [CertificateMapper](https://github.com/wtypmah24/Vessel_keeper/blob/main/src/main/java/com/marine/vessel_keeper/mapper/CertificateMapper.java):
  Handles mapping between certificate dtos and entities. 
