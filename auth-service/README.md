# Auth Service

A dedicated microservice responsible for user authentication, JWT token generation, and token validation in the Banking System. This service handles user login, token refresh, and credential management.

## Service Details

- **Service Name**: auth-service
- **Port**: 8081
- **Package**: `com.spring.auth_service`
- **Java Version**: 17

## Overview

The Auth Service provides authentication and authorization capabilities including:
- User login and credential validation
- JWT access token generation
- JWT refresh token generation and validation
- Token expiration management
- User credential management
- Integration with Spring Security

## Technology Stack

- **Framework**: Spring Boot 3.5.14
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA
- **Database Migrations**: Flyway
- **Object Mapping**: MapStruct (v1.6.3)
- **JWT Library**: JJWT (v0.13.0)
- **Security**: Spring Security
- **Validation**: Spring Boot Validation
- **Service Discovery**: Eureka Client (Netflix)
- **Build Tool**: Maven

## Dependencies

### Core Dependencies
- Spring Boot Starter Web - REST API development
- Spring Boot Starter Data JPA - Database access layer
- Spring Security - Authentication and authorization
- Spring Boot Starter Validation - Input validation
- PostgreSQL Driver - Database connectivity

### JWT & Security
- JJWT API (v0.13.0) - JWT token generation and validation
- JJWT Implementation (v0.13.0) - Runtime JWT implementation
- JJWT Jackson (v0.13.0) - Jackson integration for JWT

### Database & Utilities
- Flyway - Database schema migrations
- MapStruct (v1.6.3) - Entity to DTO mapping
- Lombok - Reduce boilerplate code

### Service Discovery
- Spring Cloud Netflix Eureka Client - Service registry integration
- Spring Security Test - Security testing utilities

## Configuration

The service uses YAML-based configuration. Key configuration file:
```
src/main/resources/application.yaml
```

### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/auth_service
    username: postgres
    password: your_password
    driver-class-name: org.postgresql.Driver
  jpa:
    show-sql: true
  hibernate:
    ddl-auto: true
  flyway:
    enabled: true
    location: classpath:db/migration
```

### JWT Configuration
```yaml
spring:
  jwt:
    secret: YOUR_SECRET_KEY
    accessTokenExpiration: 72000      # 20 hours in seconds
    refreshTokenExpiration: 604800    # 7 days in seconds
```

### Server Configuration
- **Port**: 8081
- **Application Name**: auth-service

## Project Structure

```
auth-service/
├── src/
│   ├── main/
│   │   ├── java/com/spring/auth_service/
│   │   │   ├── controller/        # REST controllers for auth endpoints
│   │   │   ├── service/           # Business logic (token generation, validation)
│   │   │   ├── repository/        # Data access layer
│   │   │   ├── entity/            # JPA entities (User, Credentials)
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── mapper/            # MapStruct mappers
│   │   │   ├── security/          # Security filters, handlers
│   │   │   ├── util/              # JWT utilities
│   │   │   ├── exception/         # Custom exceptions
│   │   │   └── config/            # Configuration classes
│   │   └── resources/
│   │       ├── application.yaml   # Main configuration
│   │       └── db/migration/      # Flyway SQL migrations
│   └── test/
│       └── java/com/spring/auth_service/
│           └── **/                # Unit and integration tests
├── pom.xml                        # Maven configuration
└── README.md                      # This file
```

## Building the Project

### Build
```bash
cd auth-service
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
cd auth-service
mvn spring-boot:run
```

### Using Java
```bash
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

### Using Docker
```bash
docker build -t auth-service:latest .
docker run -p 8081:8081 auth-service:latest
```

## Database Setup

The service uses Flyway for database migrations. Migrations are located in:
```
src/main/resources/db/migration/
```

### Prerequisites
- PostgreSQL server running on localhost:5432
- Database: `auth_service`
- User: `postgres`
- Password: `mysql` (configure as needed)

### Create Database
```sql
CREATE DATABASE auth_service;
```

Flyway will automatically run migrations on application startup.

### Database Schema
The auth_service database typically includes tables for:
- Users
- User credentials
- Login history
- Token blacklist (if implemented)

## Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=AuthServiceTest
```

### Run Security Tests
```bash
mvn test -Dtest=SecurityTest
```

## API Endpoints

### Authentication Endpoints
- `POST /api/v1/auth/login` - User login (returns access and refresh tokens)
- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/refresh` - Refresh access token
- `POST /api/v1/auth/logout` - User logout
- `POST /api/v1/auth/validate` - Validate JWT token
- `GET /api/v1/auth/user` - Get current user info

### Example Requests

#### Login
```bash
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123"
  }'
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 72000,
  "tokenType": "Bearer"
}
```

#### Refresh Token
```bash
curl -X POST http://localhost:8081/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }'
```

#### Validate Token
```bash
curl -X POST http://localhost:8081/api/v1/auth/validate \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## Security

### JWT Token Structure

**Access Token**:
- Short-lived (20 hours)
- Contains user information and permissions
- Used for API request authentication

**Refresh Token**:
- Long-lived (7 days)
- Used to obtain new access tokens
- Invalidated on logout

### JWT Payload Example
```json
{
  "sub": "user@example.com",
  "iat": 1234567890,
  "exp": 1234641890,
  "authorities": ["ROLE_USER"],
  "userId": 123
}
```

### Token Signing
- Algorithm: HS256 (HMAC SHA256)
- Secret: Configured in `application.yaml`
- Keep secret secure in production

### Password Security
- Passwords should be hashed using BCrypt or similar
- Never store plain text passwords
- Use Spring Security's PasswordEncoder

## Password Management

### Best Practices
1. Hash passwords using BCryptPasswordEncoder
2. Salt passwords automatically (BCrypt handles this)
3. Validate password strength requirements
4. Implement password expiration policies
5. Support password reset functionality

## Token Validation

### Validation Process
1. Check token signature
2. Verify token is not expired
3. Extract user information
4. Check user is active
5. Verify user permissions

### Error Responses
- 401 Unauthorized - Invalid or expired token
- 403 Forbidden - Insufficient permissions
- 400 Bad Request - Missing or malformed token

## Integration with Other Services

- **API Gateway**: Validates tokens from requests
- **User Service**: Validates user existence
- **Account Service**: Checks user permissions

## Monitoring & Logging

### Log Levels
```yaml
logging:
  level:
    org.springframework.security: DEBUG
```

### Available Logs
- Authentication attempts
- Token generation and validation
- Security filter processing
- Authorization decisions

## Security Best Practices

### Configuration
```yaml
spring:
  jwt:
    # Use strong, random secret (min 32 characters)
    secret: ${JWT_SECRET}
    # Set appropriate expiration times
    accessTokenExpiration: ${ACCESS_TOKEN_EXPIRATION}
    refreshTokenExpiration: ${REFRESH_TOKEN_EXPIRATION}
```

### Deployment Checklist
- [ ] Use strong JWT secret (minimum 32 characters)
- [ ] Store secrets in environment variables, not code
- [ ] Enable HTTPS/TLS for all endpoints
- [ ] Implement rate limiting on login endpoint
- [ ] Add CORS configuration
- [ ] Enable security headers
- [ ] Implement account lockout after failed logins
- [ ] Log security events
- [ ] Monitor for suspicious activities
- [ ] Implement token blacklisting for logout

## Troubleshooting

### Port Already in Use
```bash
# Change port in application.yaml
server:
  port: 8089
```

### Database Connection Failed
- Verify PostgreSQL is running
- Check database credentials
- Ensure database `auth_service` exists

### JWT Token Validation Errors
- Verify JWT secret matches in all services
- Check token expiration time
- Ensure token is properly formatted
- Verify Authorization header format

### Login Failures
- Check user credentials in database
- Verify user account is active
- Check password hashing algorithm
- Verify user permissions

## Environment Variables

Use environment variables or `.env` file for sensitive data:
```
JWT_SECRET=<strong-secret-key>
DB_URL=jdbc:postgresql://localhost:5432/auth_service
DB_USERNAME=postgres
DB_PASSWORD=<strong-password>
ACCESS_TOKEN_EXPIRATION=72000
REFRESH_TOKEN_EXPIRATION=604800
```

## Performance Considerations

- Cache JWT validation results
- Use connection pooling
- Optimize database queries
- Implement rate limiting
- Use caching for user lookups

## Migration & Compatibility

### Database Migrations
- Flyway migrations in `src/main/resources/db/migration/`
- File naming: `V1__Initial_schema.sql`
- Automatic execution on startup

### Backward Compatibility
- Maintain token format compatibility
- Support multiple JWT algorithms if needed
- Version APIs for changes

## Deployment

### Docker Build
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/auth-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Kubernetes Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: auth-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: auth-service
  template:
    metadata:
      labels:
        app: auth-service
    spec:
      containers:
      - name: auth-service
        image: auth-service:latest
        ports:
        - containerPort: 8081
        env:
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: auth-secrets
              key: jwt-secret
```

## Related Documentation

- Spring Security: https://spring.io/projects/spring-security
- JJWT: https://github.com/jwtk/jjwt
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Flyway: https://flywaydb.org/
- PostgreSQL: https://www.postgresql.org/

## Related Services

- [API Gateway](../api-gateway/README.md)
- [User Service](../user-service/README.md)
- [Account Service](../account-service/README.md)
- [Discovery Server](../discoveryServer/README.md)
- [Main README](../README.md)
