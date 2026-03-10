# Zero-Trust CI/CD Pipeline for Microservices

A **Spring Boot microservices demo project** showcasing a Zero-Trust security architecture with two independent services communicating via REST APIs.

## Project Structure

```
zero-trust-cicd-microservices/
├── order-service/              # Order Management Service
│   ├── src/main/java/...
│   ├── pom.xml
│   └── Dockerfile
│
├── payment-service/             # Payment Processing Service
│   ├── src/main/java/...
│   ├── pom.xml
│   └── Dockerfile
│
├── docker-compose.yml
└── README.md
```

## Services Overview

### Order Service (Port: 8080)
**Responsibility**: Manage orders and coordinate with Payment Service

**Endpoints**:
- `POST /api/orders` - Create a new order
- `GET /api/orders` - Retrieve all orders
- `GET /api/orders/health` - Health check

**Order Model**:
```json
{
  "orderId": "string (UUID)",
  "product": "string",
  "amount": "double",
  "status": "PENDING | CONFIRMED | FAILED"
}
```

### Payment Service (Port: 8081)
**Responsibility**: Process payments with 80% success simulation

**Endpoints**:
- `POST /api/payments` - Process payment
- `GET /api/payments` - Retrieve all payments
- `GET /api/payments/health` - Health check

**Payment Model**:
```json
{
  "paymentId": "string (UUID)",
  "orderId": "string",
  "amount": "double",
  "status": "SUCCESS | FAILED"
}
```

## Prerequisites

- Docker & Docker Compose
- Maven 3.9+ (for local development)
- Java 17+

## Quick Start

### Option 1: Run with Docker Compose (Recommended)

```bash
cd zero-trust-cicd-microservices
docker-compose up --build
```

Services will be available at:
- Order Service: `http://localhost:8080`
- Payment Service: `http://localhost:8081`

### Option 2: Build and Run Locally

#### Build Both Services
```bash
# Build Order Service
cd order-service
mvn clean package
java -jar target/order-service-1.0.0.jar

# In another terminal, build Payment Service
cd payment-service
mvn clean package
java -jar target/payment-service-1.0.0.jar
```

## API Testing Examples

### Health Checks

```bash
# Order Service Health
curl -X GET http://localhost:8080/api/orders/health

# Payment Service Health
curl -X GET http://localhost:8081/api/payments/health
```

### Create an Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "product": "Laptop",
    "amount": 999.99
  }'
```

**Response** (if payment successful):
```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440000",
  "product": "Laptop",
  "amount": 999.99,
  "status": "CONFIRMED"
}
```

**Response** (if payment failed):
```json
{
  "orderId": "550e8400-e29b-41d4-a716-446655440001",
  "product": "Laptop",
  "amount": 999.99,
  "status": "FAILED"
}
```

### Get All Orders

```bash
curl -X GET http://localhost:8080/api/orders
```

**Response**:
```json
[
  {
    "orderId": "550e8400-e29b-41d4-a716-446655440000",
    "product": "Laptop",
    "amount": 999.99,
    "status": "CONFIRMED"
  }
]
```

### Get All Payments

```bash
curl -X GET http://localhost:8081/api/payments
```

**Response**:
```json
[
  {
    "paymentId": "660e8400-e29b-41d4-a716-446655440001",
    "orderId": "550e8400-e29b-41d4-a716-446655440000",
    "amount": 999.99,
    "status": "SUCCESS"
  }
]
```

## Microservice Communication

The Order Service communicates with the Payment Service synchronously:

1. Client sends POST request to Order Service
2. Order Service creates an order with PENDING status
3. Order Service calls Payment Service API via RestTemplate
4. Payment Service simulates payment (80% success rate)
5. Order Service updates order status based on payment response
   - SUCCESS → Order status = CONFIRMED
   - FAILED → Order status = FAILED

## Docker Images

### Building Images Manually

```bash
# Build Order Service Image
docker build -t order-service:1.0.0 ./order-service

# Build Payment Service Image
docker build -t payment-service:1.0.0 ./payment-service

# Run with custom network
docker network create devsecops-network
docker run -d -p 8081:8081 --name payment-service --network devsecops-network payment-service:1.0.0
docker run -d -p 8080:8080 --name order-service --network devsecops-network order-service:1.0.0
```

## Stopping Services

### With Docker Compose
```bash
docker-compose down
```

### Manual Docker
```bash
docker stop order-service payment-service
docker rm order-service payment-service
```

## Code Structure

### Order Service
- **Model**: `Order.java`, `PaymentRequest.java`, `PaymentResponse.java`
- **Controller**: `OrderController.java`
- **Service**: `OrderService.java`
- **Application**: `OrderServiceApplication.java`

### Payment Service
- **Model**: `Payment.java`, `PaymentRequest.java`
- **Controller**: `PaymentController.java`
- **Service**: `PaymentService.java`
- **Application**: `PaymentServiceApplication.java`

## Key Features for DevSecOps

1. **Containerization**: Multi-stage Docker builds for minimal image size
2. **Service Isolation**: Services run in separate containers
3. **Internal Communication**: Services communicate via REST with automatic DNS resolution
4. **Health Checks**: Dedicated `/health` endpoints for monitoring
5. **Stateless Design**: No shared state between services
6. **Simple Deployment**: Single `docker-compose.yml` for orchestration

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Maven 3.9.4
- **Container**: Docker & Docker Compose
- **HTTP Client**: RestTemplate (Spring)

## Troubleshooting

### "Connection refused" when Order Service calls Payment Service
- Ensure Payment Service is running
- Check that payment-service hostname is resolvable (correct docker-compose.yml setup)
- Verify network connectivity: `docker network ls`

### Services won't start
```bash
# Check logs
docker-compose logs -f order-service
docker-compose logs -f payment-service

# Rebuild images
docker-compose up --build --force-recreate
```

### Port already in use
Change ports in `docker-compose.yml` or stop conflicting services:
```bash
docker ps -a
docker stop <container_id>
```

## Learning Outcomes

This project demonstrates:
- Microservices architecture with Spring Boot
- Inter-service REST communication
- Docker containerization
- Docker Compose orchestration
- Service discovery via DNS
- In-memory data storage
- RESTful API design
- Error handling in distributed systems

## Author
DevSecOps Learning Project

## License
MIT
