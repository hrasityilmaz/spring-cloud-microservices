
# Spring Cloud Microservices

![Eureka Dashboard](docs/images/eureka_2.png)

A microservices architecture project built with **Spring Boot 4.2.0** and **Spring Cloud**.

The application has been containerized with Docker and is currently running in Docker containers. The next step is deploying the microservices architecture to **Kubernetes using Minikube**.


## Architecture

- **API Gateway** – Single entry point for client requests. Handles request routing and JWT validation.
- **Users Service** – Manages user registration, authentication, and JWT generation.
- **Todos Service** – Manages Todo operations and associates Todos with their authenticated owners.
- **Eureka Server** – Provides service discovery and dynamic service registration.
- **Config Server** - Centralize configuration management
- **MySQL** – Persistent relational storage for Users and Todos services.
- **Apache Kafka** – Enables asynchronous event-driven communication between microservices.

All client requests are routed through the API Gateway. Internal service communication is handled through service discovery and asynchronous messaging.

## Technologies

- Java 21
- Spring Boot 4.2.0
- Spring Cloud Gateway
- Config Server
- Spring Cloud Netflix Eureka
- Spring Security
- Spring Data JPA / Hibernate
- MySQL
- Apache Kafka
- JWT

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Authenticate user and obtain JWT |
| DELETE | `/api/user` | Delete user |
| GET | `/api/user/status/check` | Check authentication status |
| POST | `/api/todos/create/todo` | Create a Todo |
| GET | `/api/todos` | List authenticated user's Todos |
| PUT | `/api/todos/{id}` | Toggle Todo completion |
| DELETE | `/api/todos/{id}` | Delete a Todo |

Protected endpoints require an access token:

```http
Authorization: Bearer <JWT_TOKEN>
```

## Configuration Management 

The project uses **Spring Cloud Config Server** to centralize configuration management across microservices.

Instead of maintaining separate configuration files for each service, configurations are stored in a dedicated GitHub repository and served by the Config Server.

- Centralized configuration management through Spring Cloud Config Server.
- Git-based configuration repository.
- Services retrieve their configurations during startup.
- Environment variables are used for sensitive configuration values .

```env
$env:DB_USERNAME="...."
$env:DB_PASSWORD="...."
$env:JWT_TOKEN="....."
```

Configuration Repository: [microservices-config](https://github.com/hrasityilmaz/microservices-config)

## Event-Driven Communication

Apache Kafka is used for reliable asynchronous communication between Users Service and Todos Service. Using at-least-once delivery instead of fire-and-forget messaging. Of course not good way for financial transactions but for todos removal pretty well working 

When a user is deleted:

1. Users Service deletes the user and creates an Outbox event in the same database transaction.
2. The Outbox Publisher publishes the pending event to Kafka.
3. Todos Service consumes the `user.deleted` event.
4. All Todos associated with the deleted user are removed.
