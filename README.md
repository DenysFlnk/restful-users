# RESTful API 'Users'
> Test task for job apply. RESTful API with endpoint "rest-api/users". Created with Spring Boot 3, Spring Data JPA, H2 DB, Open API 3.

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Setup](#setup)
* [Usage](#usage)
* [Contact](#contact)

## General Information
Description taken from test task and used for app creation:

>The task has two parts:
>1. Using the resources listed below learn what is RESTful API and what are the best practices to implement it
>2. According to the requirements implement the RESTful API based on the web Spring Boot application: controller, responsible for the resource named Users.
>
>Resources:
> 
>https://phauer.com/2015/restful-api-design-best-practices/
> 
>https://www.baeldung.com/exception-handling-for-rest-with-spring
> 
>https://www.baeldung.com/spring-boot-testing#unit-testing-with-webmvctest
> 
>https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-server
>
>
>Requirements:
>1. It has the following fields:

>   1.1. Email (required). Add validation against email pattern
>
>   1.2. First name (required)
>
>   1.3. Last name (required)
>
>   1.4. Birth date (required). Value must be earlier than current date
>
>   1.5. Address (optional)
>
>   1.6. Phone number (optional)
>
>2. It has the following functionality:
>
>   2.1. Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from properties file.
>
>   2.2. Update one/some user fields
>
>   2.3. Update all user fields
>
>   2.4. Delete user
>
>   2.5. Search for users by birthdate range. Add the validation which checks that “From” is less than “To”.  Should return a list of objects
>
>3. Code is covered by unit tests using Spring
>4. Code has error handling for REST
>5. API responses are in JSON format


## Technologies Used
- Java 17
- Spring Boot 3.1.4
- Spring Data JPA
- H2 DB
- Logback logging
- Testing:
    - JUnit 5
    - Mockito
- Open API 3 (Swagger)

## Setup
Fork this repo to your GitHub and clone to your PC. Run `RestfulUsersApplication.class` in your IDE.


## Usage
You can use and test every app feature with Swagger UI by clicking [link](http://localhost:8080/swagger-ui.html).


## Contact
Created by Denys Filonenko:
- [GitHub](https://github.com/DenysFlnk)
- [Email](mailto:filonenko.denys94@gmail.com)
- [LinkedIn](https://www.linkedin.com/in/denys-filonenko-6a8632163/)
