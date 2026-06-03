# USER-SERVICE.md (Banking Grade Design)

## 1. Service Overview

The user-service is the central identity and customer management
microservice responsible for: - Authentication (JWT + Refresh Token) -
User & Customer lifecycle management - KYC data handling - Address
management - Role-based access control (RBAC) - Security enforcement
(encryption, masking, audit-ready design)

Acts as IAM core of banking system.

## 2. Architecture Style

Hybrid Microservice Architecture + Event-Driven Ready - REST APIs
(Spring Boot) - JWT-based security Future: Kafka events

## 3. Database Design (MySQL)

### auth_user

-   id (PK)
-   email
-   password (BCrypt)
-   role (USER, CUSTOMER, EMPLOYER, ADMIN, AUDITOR)
-   status
-   created_at
-   updated_at

### customer

-   id (PK)
-   auth_user_id (FK)
-   first_name
-   last_name
-   phone
-   date_of_birth
-   employment_type

### address

-   id (PK)
-   customer_id (FK)
-   street
-   city
-   state
-   zip
-   country

### kyc_document

-   id (PK)
-   customer_id (FK)
-   document_type
-   document_number (ENCRYPTED)
-   key_version
-   created_at

## 4. Security Architecture

-   JWT Authentication
-   Refresh Token
-   BCrypt Password Encoding
-   Redis Token Blacklist
-   RBAC Authorization
-   Field Level Masking
-   Audit Logs (future)

## 5. JWT Flow

Login -\> Validate -\> Generate JWT + Refresh Token -\> Response

Request Flow: Client -\> JwtAuthFilter -\> SecurityContext -\> Service
-\> Mapper -\> Response

## 6. Roles

USER, CUSTOMER, EMPLOYER, ADMIN, AUDITOR

## 7. Field Masking

-   USER: masked data
-   EMPLOYER: partial
-   ADMIN: full access
-   AUDITOR: full + audit tag

## 8. APIs

Auth: - POST /auth/register - POST /auth/login - POST /auth/refresh -
POST /auth/logout

User: - GET /users/{id} - PUT /users/{id}

KYC: - POST /kyc/upload - GET /kyc/{id}

## 9. Future Enhancements

-   Kafka event system
-   API Gateway
-   OAuth2 login
-   ABAC security model
