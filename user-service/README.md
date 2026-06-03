# User Service

A microservice responsible for user profile management and user-related operations in the Banking System. This service handles user registration, profile updates, and user information retrieval.

## Service Details

- **Service Name**: user-service
- **Port**: 8088
- **Package**: `com.spring.user_service`
- **Java Version**: 17

## Overview

The User Service manages user profiles and associated data in the banking system. It provides REST APIs for:
- User registration
- User profile management
- User information retrieval
- Profile updates

## Technology Stack

- **Framework**: Spring Boot 3.5.14
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA
- **Database Migrations**: Flyway
- **Object Mapping**: MapStruct (v1.6.3)
- **Validation**: Spring Boot Validation
- **Service Discovery**: Eureka Client (Netflix)
- **Security**: Spring Security
- **Build Tool**: Maven

## Dependencies

### Core Dependencies
- Spring Boot Starter Web - REST API development
- Spring Boot Starter Data JPA - Database access layer
- Spring Boot Starter Validation - Input validation
- Spring Security - Authentication and authorization
- PostgreSQL Driver - Database connectivity

### Utilities
- Lombok - Reduce boilerplate code
- MapStruct - Entity to DTO mapping
- Flyway - Database migrations
- Spring Context - Dependency injection
- Spring Dotenv - Environment variable management

### Service Discovery
- Spring Cloud Netflix Eureka Client - Service registry integration

### Common Libraries
- Common Security - Shared security utilities

## Configuration

The service uses YAML-based configuration. Key configuration file:
```
src/main/resources/application.yaml
```

### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/user_service
    username: postgres
    password: YOUR_PASSWORD
  jpa:
    show-sql: true
  hibernate:
    ddl-auto: true
  flyway:
    enabled: true
    location: classpath:db/migration
```

### Server Configuration
- **Port**: 8088
- **Application Name**: user-service

### Encryption
- Active Key Version: v2
- Encryption keys configured in `application.yaml`

## Project Structure

```
user-service/
├── src/
│   ├── main/
│   │   ├── java/com/spring/user_service/
│   │   │   ├── controller/        # REST controllers
│   │   │   ├── service/           # Business logic
│   │   │   ├── repository/        # Data access layer
│   │   │   ├── entity/            # JPA entities
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── mapper/            # MapStruct mappers
│   │   │   └── config/            # Configuration classes
│   │   └── resources/
│   │       ├── application.yaml   # Main configuration
│   │       └── db/migration/      # Flyway SQL migrations
│   └── test/
│       └── java/com/spring/user_service/  # Unit and integration tests
├── pom.xml                        # Maven configuration
└── README.md                      # This file
```

## Building the Project

### Build
```bash
cd user-service
mvn clean package
```

### Skip Tests
```bash
mvn clean package -DskipTests
```

### Build JAR
```bash
mvn clean install
```

## Running the Service

### Using Maven
```bash
cd user-service
mvn spring-boot:run
```

### Using Java
```bash
java -jar target/user-service-0.0.1-SNAPSHOT.jar
```

### Using Docker
```bash
docker build -t user-service:latest .
docker run -p 8088:8088 user-service:latest
```

## Database Setup

The service uses Flyway for database migrations. Migrations are located in:
```
src/main/resources/db/migration/
```

### Prerequisites
- PostgreSQL server running on localhost:5432
- Database: `user_service`
- User: `postgres`
- Password: `mysql` (configure as needed)

### Create Database
```sql
CREATE DATABASE user_service;
```

Flyway will automatically run migrations on application startup.

## Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=UserControllerTest
```

### Run Tests with Coverage
```bash
mvn clean test jacoco:report
```

## API Endpoints

### User Management
- `GET /api/v1/customers` - Get all users
- `GET /api/v1/customers/{id}` - Get user by ID
- `POST /api/v1/customers` - Create new user
- `PUT /api/v1/customers/{id}` - Update user
- `DELETE /api/v1/customers/{id}` - Delete user

## Security

The service integrates with Spring Security and uses:
- JWT tokens for API authentication (via API Gateway)
- Spring Security filters
- Common Security module for shared security configurations

### Encryption
- Active encryption key version: v2
- Sensitive data encryption enabled

## Service Discovery

The User Service registers with Eureka Service Discovery with the name **USER-SERVICE**.

### Eureka Configuration
- Service name: user-service
- Auto-registration: enabled
- Heartbeat interval: default

## Monitoring & Logging

### Log Levels
```yaml
logging:
  level:
    org.springframework.security: DEBUG
```

### Available Logs
- Spring Security debug logs enabled
- Database SQL logs: `show-sql: true`

## Development Guidelines

### Entity Development
1. Create entity in `entity/` package
2. Create repository extending `JpaRepository`
3. Create DTO in `dto/` package
4. Create MapStruct mapper in `mapper/` package
5. Create service with business logic
6. Create REST controller

### Database Migrations
1. Create new SQL file in `src/main/resources/db/migration/`
2. Follow naming convention: `V1__Initial_schema.sql`, `V2__Add_column.sql`
3. Flyway will execute automatically on startup

## Integration with Other Services

- **API Gateway**: Routes requests to this service at `/api/v1/customers/**`
- **Auth Service**: Validates JWT tokens before requests reach this service
- **Common Security**: Uses shared security configurations

## Troubleshooting

### Port Already in Use
```bash
# Change port in application.yaml
server:
  port: 8089
```

### Database Connection Failed
- Verify PostgreSQL is running
- Check database credentials in `application.yaml`
- Ensure database `user_service` exists

### Migration Issues
- Check Flyway migration files syntax
- Verify file names follow naming convention
- Check database permissions for migrations

## Environment Variables

Use `.env` file with spring-dotenv for environment-specific configuration:
```
DB_URL=jdbc:postgresql://localhost:5432/user_service
DB_USERNAME=postgres
DB_PASSWORD=mysql
ENCRYPTION_KEY=<your-key>
```

## Performance Optimization

- Enable JPA query pagination for large datasets
- Use MapStruct for efficient DTO mapping
- Configure database connection pooling
- Enable caching where appropriate

## Deployment

### Production Checklist
- [ ] Set strong database passwords
- [ ] Configure environment-specific properties
- [ ] Enable HTTPS
- [ ] Set up proper logging
- [ ] Configure Eureka for production
- [ ] Set appropriate JVM heap size
- [ ] Enable monitoring and alerting

### Docker Build
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## Support & Documentation

- Spring Boot Docs: https://spring.io/projects/spring-boot
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Flyway: https://flywaydb.org/
- MapStruct: https://mapstruct.org/
- PostgreSQL: https://www.postgresql.org/

## Related Services

- [API Gateway](../api-gateway/README.md)
- [Auth Service](../auth-service/README.md)
- [Account Service](../account-service/README.md)
- [Main README](../README.md)
