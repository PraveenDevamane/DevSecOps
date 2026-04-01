@echo off
REM ============================================
REM DevSecOps Microservices - API Test Script
REM ============================================

setlocal enabledelayedexpansion

set API_BASE_URL=http://localhost:8080
set PAYMENT_API_BASE_URL=http://localhost:8081

echo.
echo ========================================
echo Testing Health Checks
echo ========================================

echo.
echo 1. Order Service Health Check:
curl -X GET %API_BASE_URL%/orders/health

echo.
echo.
echo 2. Payment Service Health Check:
curl -X GET %PAYMENT_API_BASE_URL%/payments/health


echo.
echo.
echo ========================================
echo Testing Order Creation
echo ========================================

echo.
echo Creating Order 1 - Gaming Laptop:
curl -X POST %API_BASE_URL%/orders ^
  -H "Content-Type: application/json" ^
  -d "{\"product\": \"Gaming Laptop\", \"amount\": 1499.99}"

echo.
echo.
echo Creating Order 2 - Monitor:
curl -X POST %API_BASE_URL%/orders ^
  -H "Content-Type: application/json" ^
  -d "{\"product\": \"Ultra HD Monitor\", \"amount\": 599.99}"

echo.
echo.
echo Creating Order 3 - Keyboard:
curl -X POST %API_BASE_URL%/orders ^
  -H "Content-Type: application/json" ^
  -d "{\"product\": \"Mechanical Keyboard\", \"amount\": 149.99}"


echo.
echo.
echo ========================================
echo Getting All Orders
echo ========================================

echo.
echo All Orders:
curl -X GET %API_BASE_URL%/orders

echo.
echo.
echo ========================================
echo Getting All Payments
echo ========================================

echo.
echo All Payments:
curl -X GET %PAYMENT_API_BASE_URL%/payments

echo.
echo.
echo ========================================
echo Test Complete!
echo ========================================
pause
