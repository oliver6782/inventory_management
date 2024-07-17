# Medication inventory management system

## Introduction:
This Spring Boot project aims to develop a medicine inventory management system designed to 
help organizations track and manage their inventory effectively.
The system will allow users to add, update, 
delete, and search inventory items, along with maintaining the stock levels.

## Tech stack:
+ Spring Boot
+ Spring AOP
+ Spring Actuator
+ Spring Security
+ Spring Data JPA
+ Spring Validator
+ Spring Async
+ Spring Cache
+ Redis
+ Maven
+ Docker
+ Swagger OpenAPI
+ JUnit & Mockito
+ Jacoco
+ Postman
+ MySQL

For details, please check the `pom.xml` for dependency coordinates.

This project initialize an `ADMIN` user at running time.

| Name  | Email           | Password     | Role  |
|-------|-----------------|--------------|-------|
| admin | admin@gmail.com | Admin123123@ | ADMIN |

note: password pattern must match these criteria:
- At least one digit [0-9]
- At least one lowercase letter [a-z]
- At least one uppercase letter [A-Z]
- At least one special character [@#$%^&+=]
- No whitespace allowed
- At least 8 characters long




