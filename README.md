# Currency exchange rates

## Overview
This project is a Spring Boot application designed to display currency exchange rates. It includes functionality such as:
- get a list of currencies used in the project;
- get exchange rates for a currency;
- add new currency for getting exchange rates.

## Prerequisites
Before running the project, ensure you have the following installed:
- Java 17 (or your required version)
- Gradle
- PostgreSQL, H2(for testing)
- Docker

## Running the Application

1. **Clone the repository**:
```bash
   https://github.com/Nokitelinho/spribe.git
   cd spribe
```
2.	**Set up the database**:
Run docker with PostgreSQL, you can start it using docker-compose:
```bash 
docker-compose up
```
3.	**Build the project**:
To build the project using Gradle, run:
```bash 
./gradlew build
```
4.	**Run the application**:
After building the project, you can run the application using:
```bash 
./gradlew bootRun
```
5.	**Access the application**:
Once the application is running, you can access it at http://localhost:8080/api/v1/currency/all

### Running Tests

To run the tests, execute:
```bash 
./gradlew test
```
## Database Configuration
```yaml
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
```