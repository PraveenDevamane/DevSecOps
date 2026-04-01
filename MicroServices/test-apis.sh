#!/bin/bash

# ============================================
# DevSecOps Microservices - API Test Script
# ============================================

API_BASE_URL="http://localhost:8080"
PAYMENT_API_BASE_URL="http://localhost:8081"

echo "========================================"
echo "Testing Health Checks"
echo "========================================"

echo -e "\n1. Order Service Health Check:"
curl -s -X GET http://localhost:8080/actuator/health

echo -e "\n\n2. Payment Service Health Check:"
curl -s -X GET http://localhost:8081/actuator/health


echo -e "\n\n========================================"
echo "Testing Order Creation (Payment Success)"
echo "========================================"

ORDER_RESPONSE=$(curl -s -X POST $API_BASE_URL/orders \
  -H "Content-Type: application/json" \
  -d '{
    "product": "Gaming Laptop",
    "amount": 1499.99
  }')

echo -e "\nOrder Created:"
echo $ORDER_RESPONSE | jq .


echo -e "\n\n========================================"
echo "Testing Order Creation (Multiple Orders)"
echo "========================================"

for i in {1..3}; do
  PRODUCT="Product_$i"
  AMOUNT=$((100 + i * 50))
  
  echo -e "\nCreating Order $i:"
  curl -s -X POST $API_BASE_URL/orders \
    -H "Content-Type: application/json" \
    -d "{
      \"product\": \"$PRODUCT\",
      \"amount\": $AMOUNT
    }" | jq .
done


echo -e "\n\n========================================"
echo "Getting All Orders"
echo "========================================"

echo -e "\nAll Orders:"
curl -s -X GET $API_BASE_URL/orders | jq .


echo -e "\n\n========================================"
echo "Getting All Payments"
echo "========================================"

echo -e "\nAll Payments:"
curl -s -X GET $PAYMENT_API_BASE_URL/payments | jq .


echo -e "\n\n========================================"
echo "Test Complete!"
echo "========================================"
