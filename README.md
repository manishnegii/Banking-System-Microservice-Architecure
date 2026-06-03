# Banking System

A microservices-based banking application built with Spring Boot and Spring Cloud. This system provides comprehensive banking operations including account management, transactions, authentication, auditing, and notifications.

## Project Structure

This is a multi-module Maven project with the following services:

### Core Services

- **account-service** - Account management and operations
- **transaction-service** - Transaction processing and management
- **user-service** - User profile and management
- **auth-service** - Authentication and authorization
- **ledger-service** - Financial ledger and balance tracking
- **notification-service** - Notification delivery (email, SMS, etc.)
- **audit-service** - Audit logging and compliance

### Infrastructure

- **api-gateway** - API Gateway for routing and load balancing
- **discoveryServer** - Service discovery (Eureka)
- **common-security** - Shared security configurations and utilities

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- Docker (optional, for containerized deployment)

## Building the Project

### Build All Modules

```bash
mvn clean package
```

### Build Specific Service

```bash
cd <service-name>
mvn clean package
```

## Running Services

Each service can be run individually using Maven or as a Docker container:

```bash
cd <service-name>
mvn spring-boot:run
```

## Service Ports

| Service | Port |
|---------|------|
| API Gateway | 8080 |
| Discovery Server | 8761 |
| Account Service | 8081 |
| User Service | 8082 |
| Transaction Service | 8083 |
| Auth Service | 8084 |
| Ledger Service | 8085 |
| Notification Service | 8086 |
| Audit Service | 8087 |

## Architecture

This system uses:
- **Service Discovery** - Eureka for dynamic service registration
- **API Gateway** - For unified API access point
- **Security** - JWT-based authentication with OAuth2
- **Communication** - REST APIs with asynchronous messaging
- **Database** - Microservice-specific databases (Database per service pattern)

## Configuration

Each service has its own `application.yaml` or `application.properties` configuration file located in:
```
<service-name>/src/main/resources/
```

## Testing

Run tests for all modules:
```bash
mvn test
```

Run tests for specific service:
```bash
cd <service-name>
mvn test
```

## Documentation

- Each service has a `HELP.md` file with service-specific documentation
- See [account-service/AccountGenerator.md](account-service/AccountGenerator.md) for account generation utilities

## Development

### Project Structure
- `src/main/java/` - Source code
- `src/main/resources/` - Configuration files
- `src/test/java/` - Test classes
- `target/` - Compiled artifacts

### Common Dependencies
- Spring Boot
- Spring Cloud
- Spring Security
- Spring Data JPA
- Maven plugins for building and testing

## Deployment

Services can be deployed as:
- Standalone JAR files
- Docker containers
- Kubernetes pods

## Support

For service-specific information, refer to the individual HELP.md files in each service directory.

## License

[Add your license information here]
