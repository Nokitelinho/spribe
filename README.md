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
- Postman (for API testing)

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
### Database Configuration
```yaml
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
```

### Liquibase Database Migrations
This project uses Liquibase for managing database migrations. Migration scripts are stored in the src/main/resources/db/changelog/ directory. You can use the following commands with Gradle to manage migrations:
- Apply migrations:
```bash 
./gradlew update
```
- Rollback migrations (example: rollback the last migration):
```bash 
./gradlew rollbackCount -PliquibaseCommandValue=1
```
- Generate a changelog for an existing database:
```bash 
./gradlew generateChangelog
```
### Using Postman for API Testing
#### Importing the Postman Collection

1.	Download and install Postman.
2. Open Postman and import the demo.postman_collection.json file provided in this repository.
3. This collection contains requests for CRUD operations on the Currency API.

#### Available Endpoints

- GET All Currencies:
- -	URL: http://localhost:8080/api/v1/currency/all
- -	Method: GET
- -	Description: Fetches all currency data from the system.
- -	Response: Status 200 OK with JSON data of currencies.
- GET Specific Currency Rate:
- - URL: http://localhost:8080/api/v1/currency/rate?currencyCode=EUR
- - Method: GET
- - Description: This retrieves currency rates (currently set to fetch all, can be modified to fetch specific rates).
- - Response: Status 200 OK with JSON data.
- POST Add a Currency:
- - URL: http://localhost:8080/api/v1/currency/add
- - Method: POST
- - Body:

```json 
{
	"success": true,
	"timestamp": 1519296206,
	"base": "EUR",
	"date": "2021-03-17",
	"rates": {
		"AUD": 1.566015,
		"CAD": 1.560132,
		"CHF": 1.154727,
		"CNY": 7.827874,
		"GBP": 0.882047,
		"JPY": 132.360679,
		"USD": 1.23396
	}
}
```

## Retry Mechanism with `@Retryable`

This project includes a retry mechanism using Spring's `@Retryable` annotation, allowing certain methods to retry upon encountering specific exceptions. This is particularly useful when making HTTP calls to external services that might experience intermittent failures.

### Example Code with `@Retryable`

The following is an example of the `WebClientService` class, which utilizes the `@Retryable` annotation to retry the HTTP request when a `HttpClientErrorException` occurs (typically a 5xx server error). The method will retry up to 3 times with a delay of 2 seconds between attempts.

```java
@Service
@RequiredArgsConstructor
public class WebClientService {
    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Retryable(
            value = { HttpClientErrorException.class }, // Retry on server errors (5xx)
            maxAttempts = 3, // Retry up to 3 times
            backoff = @Backoff(delay = 2000) // Wait for 2 seconds between retries
    )
    public CurrencyDTO fetchCurrencyData(String requestUrl) {
        log.info("Entering WebClientService fetchCurrencyData - {}", requestUrl);
        .....
```