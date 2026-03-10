# Zero-Trust CI/CD Microservices - API Examples

## Prerequisites
- Services running on localhost:8080 (Order) and localhost:8081 (Payment)
- `curl` or Postman installed

---

## 1. HEALTH CHECKS

### Order Service Health
```bash
curl -X GET http://localhost:8080/api/orders/health
```

**Response:**
```json
{
  "status": "UP",
  "service": "Order Service"
}
```

### Payment Service Health
```bash
curl -X GET http://localhost:8081/api/payments/health
```

**Response:**
```json
{
  "status": "UP",
  "service": "Payment Service"
}
```

---

## 2. ORDER OPERATIONS

### Create Order - Single Request
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "product": "Gaming Laptop",
    "amount": 1499.99
  }'
```

**Request Body:**
```json
{
  "product": "Gaming Laptop",
  "amount": 1499.99
}
```

**Response (Success - Order Confirmed):**
```json
{
  "orderId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "product": "Gaming Laptop",
  "amount": 1499.99,
  "status": "CONFIRMED"
}
```

**Response (Failure - Order Failed):**
```json
{
  "orderId": "f47ac10b-58cc-4372-a567-0e02b2c3d480",
  "product": "Gaming Laptop",
  "amount": 1499.99,
  "status": "FAILED"
}
```

### Create Multiple Orders (Bash Loop)
```bash
for i in {1..5}; do
  curl -X POST http://localhost:8080/api/orders \
    -H "Content-Type: application/json" \
    -d "{\"product\": \"Product_$i\", \"amount\": $((100 * i))}"
  echo ""
done
```

### Get All Orders
```bash
curl -X GET http://localhost:8080/api/orders
```

**Response:**
```json
[
  {
    "orderId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "product": "Gaming Laptop",
    "amount": 1499.99,
    "status": "CONFIRMED"
  },
  {
    "orderId": "f47ac10b-58cc-4372-a567-0e02b2c3d480",
    "product": "Monitor",
    "amount": 599.99,
    "status": "FAILED"
  }
]
```

---

## 3. PAYMENT OPERATIONS

### Get All Payments
```bash
curl -X GET http://localhost:8081/api/payments
```

**Response:**
```json
[
  {
    "paymentId": "550e8400-e29b-41d4-a716-446655440000",
    "orderId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "amount": 1499.99,
    "status": "SUCCESS"
  },
  {
    "paymentId": "550e8400-e29b-41d4-a716-446655440001",
    "orderId": "f47ac10b-58cc-4372-a567-0e02b2c3d480",
    "amount": 599.99,
    "status": "FAILED"
  }
]
```

---

## 4. TESTING WORKFLOW

### Step 1: Verify Services are Running
```bash
# Check Order Service
curl http://localhost:8080/api/orders/health

# Check Payment Service
curl http://localhost:8081/api/payments/health
```

### Step 2: Create Orders
```bash
# Create order 1
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"product": "GPU", "amount": 899.99}'

# Create order 2
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"product": "RAM", "amount": 199.99}'
```

### Step 3: Verify Orders and Payments
```bash
# View all orders
curl http://localhost:8080/api/orders

# View all payments
curl http://localhost:8081/api/payments
```

---

## 5. POSTMAN COLLECTION

### Import as Postman Collection

```json
{
  "info": {
    "name": "DevSecOps Microservices",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Health Checks",
      "item": [
        {
          "name": "Order Service Health",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/orders/health"
          }
        },
        {
          "name": "Payment Service Health",
          "request": {
            "method": "GET",
            "url": "http://localhost:8081/api/payments/health"
          }
        }
      ]
    },
    {
      "name": "Orders",
      "item": [
        {
          "name": "Create Order",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/orders",
            "body": {
              "mode": "raw",
              "raw": "{\"product\": \"Gaming Laptop\", \"amount\": 1499.99}"
            }
          }
        },
        {
          "name": "Get All Orders",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/orders"
          }
        }
      ]
    },
    {
      "name": "Payments",
      "item": [
        {
          "name": "Get All Payments",
          "request": {
            "method": "GET",
            "url": "http://localhost:8081/api/payments"
          }
        }
      ]
    }
  ]
}
```

---

## 6. EXPECTED BEHAVIOR

### Order Creation Flow

1. **Request arrives** → Order Service receives POST request
2. **Order created** → Generated orderId, status = PENDING
3. **Payment called** → Order Service calls Payment Service API
4. **Payment processed** → Payment Service returns SUCCESS (80%) or FAILED (20%)
5. **Order updated** → Status changed to CONFIRMED or FAILED
6. **Response sent** → Client receives updated order

### Example Trace

Request:
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"product": "Laptop", "amount": 1499.99}'
```

Possible Response 1 (Payment Succeeded):
```json
{
  "orderId": "a1b2c3d4-e5f6-4a5b-9c8d-7e6f5a4b3c2d",
  "product": "Laptop",
  "amount": 1499.99,
  "status": "CONFIRMED"
}
```

Possible Response 2 (Payment Failed):
```json
{
  "orderId": "b2c3d4e5-f6a7-5b6c-0d9e-8f7a6b5c4d3e",
  "product": "Laptop",
  "amount": 1499.99,
  "status": "FAILED"
}
```

---

## 7. TROUBLESHOOTING

### Connection Refused
```bash
# Ensure services are running
docker ps

# Check if ports are open
netstat -an | grep 8080
netstat -an | grep 8081
```

### Service Not Responding
```bash
# Check container logs
docker logs order-service
docker logs payment-service

# Restart containers
docker restart order-service payment-service
```

### JSON Parse Error
- Ensure `-H "Content-Type: application/json"` is included in POST requests
- Validate JSON syntax: https://jsonlint.com/

---

## 8. RUNNING TEST SCRIPTS

### Bash Script
```bash
chmod +x test-apis.sh
./test-apis.sh
```

### Batch Script (Windows)
```cmd
test-apis.bat
```

---

## Notes
- Payment Service randomly returns SUCCESS (80%) or FAILED (20%)
- All data is stored in-memory; restarting services will clear data
- Services discover each other via DNS (docker-compose networking)
