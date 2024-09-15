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

## Caching Mechanism

This project includes caching functionality using Spring's `@Cacheable` and `@CacheEvict` annotations to improve performance and reduce the number of redundant database queries.

### Example Code with Caching

In the `CurrencyController` class located at `com.example.demo.CurrencyController`, caching is applied to some endpoints that retrieve currency data. This allows results to be stored in a cache and reused on subsequent calls without querying the database again.

#### Example:
```java
@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    // Cache the result of the getAllCurrencies method
    @GetMapping("/all")
    @Cacheable("currency")
    public Iterable<Currency> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    // Cache the result of the getExchangeRate method based on the currency code
    @GetMapping("/rate")
    @Cacheable("currency")
    public List<ExchangeRate> getExchangeRate(@RequestParam(name = "currencyCode") @NotBlank String currencyCode) {
        return currencyService.getExchangeRate(currencyCode);
    }

    // Clear the cache when a new currency is added
    @PostMapping("/add")
    @CacheEvict(value = "currency", allEntries = true)
    public Currency addCurrency(@RequestBody CurrencyDTO currencyDTO) {
        return currencyService.addCurrency(currencyDTO);
    }
}
```

### Explanation of Caching:
- **@Cacheable(“currency”):** This annotation indicates that the result of the method will be cached. In the example, the getAllCurrencies() and getExchangeRate() methods are cached under the “currency” cache. When the method is called, Spring first checks if the result is already cached and returns it if available. Otherwise, the method is executed, and the result is stored in the cache.
- **@CacheEvict(value = “currency”, allEntries = true):** This annotation is used to clear the cache when the addCurrency() method is called. It ensures that outdated data is not cached when new currency data is added.

### Testing Caching with Postman
You can use Postman to test the caching mechanism by:
1.	Fetching All Currencies (GET): The first time you call the GET /api/v1/currency/all endpoint, it will retrieve data from the database and cache it. Subsequent calls will return the cached result.
```bash 
GET http://localhost:8080/api/v1/currency/all
```
2.	**Fetching Exchange Rate by Currency Code (GET):** This endpoint is also cached based on the currency code you provide. For example:
```bash 
GET http://localhost:8080/api/v1/currency/rate?currencyCode=USD
```

3.	Adding New Currency (POST): When you add a new currency using the POST /api/v1/currency/add endpoint, the cache is cleared to ensure updated data.

```bash 
POST http://localhost:8080/api/v1/currency/add
```
