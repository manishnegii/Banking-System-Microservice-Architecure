# API Gateway

The central entry point for all client requests in the Banking System. This service routes requests to appropriate microservices, handles cross-cutting concerns like authentication, rate limiting, and request/response transformation.

## Service Details

- **Service Name**: api-gateway
- **Port**: 8080
- **Package**: `com.spring.api_gateway`
- **Java Version**: 17
- **Application Type**: Reactive (WebFlux)

## Overview

The API Gateway is a Spring Cloud Gateway that provides:
- Single entry point for all microservices
- Request routing based on URL patterns
- JWT token validation and authentication
- Request/response transformation
- Cross-Origin Resource Sharing (CORS) configuration
- Rate limiting and circuit breaker patterns
- Service discovery integration with Eureka

## Technology Stack

- **Framework**: Spring Boot 3.5.14
- **Language**: Java 17
- **Gateway**: Spring Cloud Gateway (Server WebFlux)
- **Service Discovery**: Netflix Eureka Client
- **Authentication**: Spring Security + JWT (JJWT)
- **API Type**: Reactive (Project Reactor WebFlux)
- **Build Tool**: Maven
- **Spring Cloud Version**: 2025.0.2

## Dependencies

### Core Gateway Dependencies
- Spring Cloud Gateway Server WebFlux - Reactive gateway framework
- Spring Cloud Netflix Eureka Client - Service discovery

### Security
- Spring Security - Authentication and authorization
- JJWT (JWT Library) - JWT token processing
  - jjwt-api (v0.13.0)
  - jjwt-impl (v0.13.0)
  - jjwt-jackson (v0.13.0)

### Testing & Utilities
- Spring Boot Starter Test - Unit testing
- Lombok - Reduce boilerplate code

## Configuration

The service uses YAML-based configuration. Key configuration file:
```
src/main/resources/application.yaml
```

### Server Configuration
```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway
  main:
    web-application-type: reactive  # Enable WebFlux
```

### Service Routes

Routes are configured to forward requests to microservices:

```yaml
spring:
  cloud:
    gateway:
      server:
        webflux:
          routes:
            - id: USER
              uri: lb://USER-SERVICE
              predicates:
                - Path=/api/v1/customers/**

            - id: account-service
              uri: lb://account-service
              predicates:
                - Path=/api/v1/account/**

            - id: transaction-service
              uri: lb://transaction-service
              predicates:
                - Path=/api/v1/transaction/**
```

- `lb://` prefix indicates load-balanced service from Eureka
- Routes use URL path predicates
- Service names must match Eureka registration names

## Project Structure

```
api-gateway/
├── src/
│   ├── main/
│   │   ├── java/com/spring/api_gateway/
│   │   │   ├── config/            # Gateway configuration
│   │   │   ├── filter/            # Request/response filters
│   │   │   ├── security/          # Security configuration
│   │   │   ├── util/              # Utility classes
│   │   │   ├── exception/         # Exception handlers
│   │   │   └── Application.java   # Main application class
│   │   └── resources/
│   │       ├── application.yaml   # Main configuration
│   │       └── application-*.yaml # Environment-specific config
│   └── test/
│       └── java/com/spring/api_gateway/  # Tests
├── pom.xml                        # Maven configuration
└── README.md                      # This file
```

## Building the Project

### Build
```bash
cd api-gateway
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

### Prerequisites
- Discovery Server running on port 8761
- At least one microservice registered with Eureka

### Using Maven
```bash
cd api-gateway
mvn spring-boot:run
```

### Using Java
```bash
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```

### Using Docker
```bash
docker build -t api-gateway:latest .
docker run -p 8080:8080 api-gateway:latest
```

## Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=GatewayRouteTest
```

## API Routes

### User Service Routes
```
GET    /api/v1/customers           → USER-SERVICE
GET    /api/v1/customers/{id}      → USER-SERVICE
POST   /api/v1/customers           → USER-SERVICE
PUT    /api/v1/customers/{id}      → USER-SERVICE
DELETE /api/v1/customers/{id}      → USER-SERVICE
```

### Account Service Routes
```
GET    /api/v1/accounts            → account-service
GET    /api/v1/accounts/{id}       → account-service
POST   /api/v1/accounts            → account-service
```

### Transaction Service Routes
```
GET    /api/v1/transactions        → transaction-service
POST   /api/v1/transactions        → transaction-service
```

## Security & Authentication

### JWT Token Validation
- All requests are validated for JWT tokens
- Invalid or expired tokens are rejected with 401 Unauthorized
- JWT tokens are extracted from Authorization header

### Security Configuration
```java
// Spring Security configuration in config package
// Protects all routes with authentication
// Validates JWT tokens before forwarding requests
```

### Token Format
```
Authorization: Bearer <jwt-token>
```

## Gateway Filters

### Global Filters
- Pre-filters: Request validation, logging
- Route filters: Service-specific processing
- Post-filters: Response transformation, logging

### Custom Filters
- JWT validation filter
- Request/response logging
- Error handling filters

## CORS Configuration

Configure CORS in `application.yaml`:
```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - name: RequestRateLimiter
          args:
            redis-rate-limiter.replenishRate: 100
            redis-rate-limiter.burstCapacity: 200
```

## Load Balancing

The gateway uses Eureka's built-in load balancing:
- Round-robin load balancing by default
- Service instances automatically discovered
- Failed requests retried on healthy instances

## Circuit Breaker Pattern

Implement circuit breakers for resilience:
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: USER
          uri: lb://USER-SERVICE
          predicates:
            - Path=/api/v1/customers/**
          filters:
            - name: CircuitBreaker
              args:
                name: userServiceCircuitBreaker
                fallbackUri: forward:/fallback
```

## Rate Limiting

Configure request rate limiting (if enabled):
- Define rate limit rules per route
- Prevent resource exhaustion
- Return 429 Too Many Requests when limits exceeded

## Request/Response Transformation

### Request Headers
- Add correlation IDs for request tracing
- Pass user information in headers
- Include JWT token information

### Response Headers
- Add CORS headers
- Add security headers
- Include response metadata

## Service Discovery Integration

The API Gateway integrates with Eureka Service Discovery:
- Automatically discovers registered microservices
- Routes requests based on service names
- Handles service instance changes dynamically

### Eureka Configuration
```yaml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    preferIpAddress: true
```

## Monitoring & Logging

### Actuator Endpoints (if enabled)
```
GET /actuator/health
GET /actuator/metrics
GET /actuator/routes
```

### Logging Configuration
```yaml
logging:
  level:
    org.springframework.cloud.gateway: DEBUG
    org.springframework.security: DEBUG
```

## Troubleshooting

### Service Not Routing
1. Check if microservice is registered with Eureka
2. Verify route configuration matches microservice registration
3. Check Eureka dashboard: http://localhost:8761

### 502 Bad Gateway
- Microservice is down or unreachable
- Check service health in Eureka
- Verify network connectivity

### JWT Token Validation Errors
- Verify token in Authorization header
- Check token expiration
- Validate token signature

### Port Already in Use
```bash
# Change port in application.yaml
server:
  port: 8081
```

## Performance Optimization

- Use reactive WebFlux for non-blocking I/O
- Enable compression for response bodies
- Configure connection pooling
- Implement caching for route metadata

## Environment-Specific Configuration

### Development
```yaml
# application-dev.yaml
logging:
  level:
    org.springframework.cloud.gateway: DEBUG
```

### Production
```yaml
# application-prod.yaml
logging:
  level:
    org.springframework.cloud.gateway: INFO
```

## Deployment

### Docker Build
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/api-gateway-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Production Checklist
- [ ] Configure HTTPS/TLS
- [ ] Set strong JWT secret keys
- [ ] Enable rate limiting
- [ ] Configure CORS appropriately
- [ ] Set up monitoring and alerting
- [ ] Enable audit logging
- [ ] Configure request/response logging
- [ ] Set up circuit breakers
- [ ] Implement proper error responses

## Advanced Configuration

### WebFlux Customization
```yaml
spring:
  webflux:
    base-path: /api
    max-in-memory-buffer-size: 16777216  # 16MB
```

### Reactive Stream Configuration
- Backpressure handling
- Subscription timeout
- Thread pool configuration

## Health Checks

Configure health endpoint for load balancers:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health
  endpoint:
    health:
      show-details: when-authorized
```

## Integration with Other Services

- **Discovery Server**: Service registration and discovery
- **Auth Service**: JWT token validation
- **User Service**: Customer management routes
- **Account Service**: Account operations routes
- **Transaction Service**: Transaction processing routes

## Related Documentation

- Spring Cloud Gateway: https://spring.io/projects/spring-cloud-gateway
- Spring Security: https://spring.io/projects/spring-security
- JJWT: https://github.com/jwtk/jjwt
- Project Reactor: https://projectreactor.io/
- Netflix Eureka: https://github.com/Netflix/eureka

## Related Services

- [Auth Service](../auth-service/README.md)
- [User Service](../user-service/README.md)
- [Account Service](../account-service/README.md)
- [Discovery Server](../discoveryServer/README.md)
- [Main README](../README.md)
