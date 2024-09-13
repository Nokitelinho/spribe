# Spring Boot Application

## Overview

This Spring Boot application is designed to handle exchange rates from external public sources" or "support high-load requests with modular microservices. The application utilizes Spring Boot, Maven for build management, and is set up for high-performance operations. Additionally, Liquibase is used for database migrations to manage and apply changes to the database schema.

## Features

- [Feature 1: "Modular architecture"]
- [Feature 2: "Integration with external APIs for exchange rates"]
- [Feature 3: "Data update automatically every hour"]

## Prerequisites

Before running the application, ensure you have the following installed:

- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- [Docker](https://www.docker.com/get-started) (for running PostgreSQL with Docker Compose)

## Getting Started

To get the project up and running on your local machine, follow these steps:

### Clone the Repository

```bash
git clone https://github.com/Nokitelinho/spribe.git
cd your-repository
```
## Build the Project

Use Maven to build the project. This command will compile the code, run tests, and package the application:

```bash
mvn clean install
```

## Set Up PostgreSQL

To run a PostgreSQL database, you can use the provided docker-compose.yml file. It sets up a PostgreSQL container with the necessary configuration.

- Make sure Docker is running on your machine.
- Run the following command to start the PostgreSQL container:

```bash
docker-compose up
```

The docker-compose.yml file will create a PostgreSQL instance that the Spring Boot application can connect to.

## Run the Application

From SpringBoot run src/main/java/com/spribe/App.java file.

OR

Run the application using Maven:

```bash
mvn spring-boot:run
```

Alternatively, you can build a runnable JAR and execute it:

```bash
mvn package
java -jar target/exchange-rate-0.0.1-SNAPSHOT.jar
```

## Access the Application

Once the application is running, you can access it at http://localhost:8080 

## API Endpoints

Use EUR for testing as the primary currency

### Get Base Currencies List

	•	Endpoint: GET /api/currency/all
	•	Description: Retrieves a list of all base currencies.
	•	Cache: Results are cached with the key "currencies".
	•	Response: A list of strings representing base currency codes.

### Get Exchange Rates

	•	Endpoint: GET /api/currency/rates
	•	Parameters:
	•	code (required): The base currency code to retrieve exchange rates for.
	•	Description: Retrieves exchange rates for the specified base currency.
	•	Cache: Results are cached with the key "currencies".
	•	Response: A list of ExchangeRateDTO objects representing exchange rates.

### Save Currency

	•	Endpoint: POST /api/currency/save
	•	Request Body: CurrencyDTO object containing currency details.
	•	Description: Saves the currency data to the database.
	•	Cache: The "currencies" cache is cleared upon saving.
	•	Response: The saved Currency object.

### Add Base Currency Code

	•	Endpoint: POST /api/currency/add
	•	Request Body: BaseCurrencyDTO object containing the base currency code to add.
	•	Description: Adds a new base currency code to the system.
	•	Cache: The "currencies" cache is cleared upon adding.
	•	Response: The Currency object representing the newly added base currency.

### Testing

To run tests, use the following Maven command:

```bash
mvn test
```
This will execute both unit and functional tests to ensure the application’s functionality.

## Database Migrations

The application uses Liquibase for database migrations. To apply migrations, use:

```bash
mvn liquibase:update
```

Liquibase helps in managing and applying changes to the database schema, ensuring that the database is in sync with the application.

## Postman Collection

A Postman collection is provided to help you test the API endpoints easily. You can import the **postman-collection.json** file into Postman to get started:

- Open Postman.
- Click on the “Import” button.
- Select the postman-collection.json file from your project directory.
- Click “Open” to import the collection.

The imported collection will include pre-configured requests for all the API endpoints.

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests with your changes. Ensure that all tests pass before submitting.

## License

This project is licensed under the MIT License.

## Contact

For any questions or issues, please contact nokitelinho@gmail.com.