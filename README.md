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
9. [VessekFinder Service](#vesselfinder-service)
10. [Third Party API Documentation](#api-doc)

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
- Third party API connection - Spring Cloud Feign
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

The Authentication Service enables users Registration/Authentication/Authorization

- [TokenService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Authorization_Service/src/main/java/com/example/authorization_service/service/token/TokenService.java):
  Provides methods for generating JWT tokens.
- [TokenValidationService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Authorization_Service/src/main/java/com/example/authorization_service/service/token/TokenValidationService.java):
  Provides methods for validating JWT tokens.
- [AuthService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Authorization_Service/src/main/java/com/example/authorization_service/service/auth/AuthService.java):
  Provides authentication-related services.
  It listens for messages containing authentication tokens from RabbitMQ and validates the tokens.  
- [UserSecurityService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Authorization_Service/src/main/java/com/example/authorization_service/service/user/UserSecurityService.java):
 The UserSecurityService class implements the UserDetailsService interface to provide user authentication details.
 It retrieves user details from the UserRepository and wraps them with UserDetails for Spring Security.
- [UserService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Authorization_Service/src/main/java/com/example/authorization_service/service/user/UserService.java):
 The UserService class provides methods for user management, such as user registration, creation, retrieval, and deletion.
 It interacts with the UserRepository to perform database operations and uses the UserMapper for mapping between DTOs and entities.

## Gateway Service <a name="gate-service"></a> 
The Gateway Service configures the routes for all services
and defines route rules using RouteLocatorBuilder and applies token validation 
filters to secure certain routes.

- [TokenCheckService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Gateway_Service/src/main/java/com/example/gateway_service/service/TokenCheckService.java):
  The TokenCheckService class is responsible for sending token verification requests 
  to the authentication service via RabbitMQ.
  It utilizes a RabbitTemplate to send and receive messages.

- [TokenValidationToken](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Gateway_Service/src/main/java/com/example/gateway_service/config/gateway/TokenValidationFilter.java):
  The TokenValidationFilter class is a GatewayFilter used for token validation and access control in the gateway service.
  It extracts the token from the request, sends it to the TokenCheckService for validation, and checks access based on the user's role.

## Vessel Service <a name="vessel-service"></a>

The Vessel Service enables users to manage vessel-related information.

- [VesselService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Vessel_Service/src/main/java/com/marine/vessel_service/service/VesselService.java):
  Manages vessel creation, deletion, and retrieval.
- [CrewService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Vessel_Service/src/main/java/com/marine/vessel_service/service/CrewService.java):
  The CrewService class handles operations related to crew management for vessels.
  It listens for messages on RabbitMQ queues to hire or fire crew members for vessels.
- [VoyageService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Vessel_Service/src/main/java/com/marine/vessel_service/service/VoyageService.java):
  The VoyageService class handles operations related to voyages and vessels.
  It listens for messages from RabbitMQ to find vessels by voyage ID and assign voyages to vessels.  
- [VesselRepository](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Vessel_Service/src/main/java/com/marine/vessel_service/repository/VesselRepository.java):
  Provides data access methods for vessel entities.
- [VesselMapper](https://github.com/wtypmah24/Vessel_keeper_microservices/tree/main/Vessel_Service/src/main/java/com/marine/vessel_service/mapper):
  Handles mapping between vessel DTOs and entities.


## Voyage Service <a name="voyage-service"></a>

The Voyage Service facilitates the management of voyage details such as ports of loading and discharging,
start and end dates, and vessel assignments.

- [VoyageService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Voyage_Service/src/main/java/com/marine/voyage_service/service/VoyageService.java):
  Manages voyage creation, deletion, and retrieval.
- [VoyageRepository](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Voyage_Service/src/main/java/com/marine/voyage_service/repository/VoyageRepository.java):
  Provides data access methods for voyage entities.
- [VoyageMapper](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Voyage_Service/src/main/java/com/marine/voyage_service/mapper/VoyageMapper.java):
  Handles mapping between voyage DTOs and entities.

## Crew Service <a name="crew-service"></a>

The Crew Service allows users to manage seaman-related information, including certificates
and service records.

- [SeamanService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Crew_Service/src/main/java/com/example/crew_service/service/seaman/SeamanService.java):
  Manages seaman creation, deletion, and retrieval.
- [SeamanRepository](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Crew_Service/src/main/java/com/example/crew_service/repository/SeamanRepository.java):
  Provides data access methods for seaman entities.
- [SeamanMapper](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Crew_Service/src/main/java/com/example/crew_service/mapper/SeamanMapper.java):
  Handles mapping between seaman DTOs and entities.
- [RecordOfServiceService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Crew_Service/src/main/java/com/example/crew_service/service/seaman/RecordOfServiceService.java):
  Manages seamen's record of services.
- [RecordOfServiceRepository](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Crew_Service/src/main/java/com/example/crew_service/repository/RecordOfServiceRepository.java):
  Provides data access methods for record of services.
- [RecordOfServiceMapper](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Crew_Service/src/main/java/com/example/crew_service/mapper/RecordOfServiceMapper.java):
  Handles mapping between record of services dto and entity.
- [CertificateService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Crew_Service/src/main/java/com/example/crew_service/service/seaman/CertificateService.java):
  Manages seamen's certificate adding and deleting.
- [CertificateRepository](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Crew_Service/src/main/java/com/example/crew_service/repository/CertificateRepository.java):
  Provides data access methods for seamen's certificates entities.
- [CertificateMapper](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/Crew_Service/src/main/java/com/example/crew_service/mapper/CertificateMapper.java):
  Handles mapping between certificate dtos and entities. 

  ## VesselFinder Service <a name="vesselfinder-service"></a>
- [VesselFinderService](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/VesselFinder_Service/src/main/java/com/example/vesselfinder_service/service/VesselFinderService.java):
Provides functionality to retrieve real-time vessel information from a remote service (VesselFinder)
- [PositionSevice](https://github.com/wtypmah24/Vessel_keeper_microservices/blob/main/VesselFinder_Service/src/main/java/com/example/vesselfinder_service/service/PositionService.java): 
Provides functionality to generate a static Google Maps URL
with markers for the positions of vessels.

## Third Party API Documentation <a name="api-doc"></a>
- [Vessel Finder](https://api.vesselfinder.com/docs/)
- [Google](https://developers.google.com/maps/documentation/maps-static/start)
