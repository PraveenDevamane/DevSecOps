 🚀 DevSecOps Microservices CI/CD Pipeline

 📌 Project Overview

This project demonstrates a **DevSecOps pipeline implementation for a microservices architecture**.
The application consists of two Spring Boot microservices:

* **Order Service**
* **Payment Service**

The goal of the project is to demonstrate how **modern DevSecOps practices** can automate software delivery using:

* GitHub
* GitHub Actions CI/CD
* Maven
* Docker
* Docker Hub

The pipeline automatically:

1. Builds the microservices
2. Creates Docker container images
3. Pushes images to Docker Hub
4. Prepares the system for security scanning and Kubernetes deployment

---

 👥 Team Responsibilities

 Member 1 — Microservices Development

Responsible for backend services.

Implemented:

* Spring Boot Order Service
* Spring Boot Payment Service
* REST API endpoints
* Inter-service communication
* Dockerfiles for both services
* Docker Compose for local development

Technologies:

* Java 17
* Spring Boot
* Maven
* REST APIs
* Docker

---

Member 2 — CI/CD Pipeline & Docker Containerization

Responsible for **CI/CD automation and containerization**.

Tasks implemented:

* GitHub Actions pipeline
* Maven build automation
* Docker image creation
* Docker Hub publishing
* GitHub Secrets configuration
* Pipeline verification

---

 Member 3 — Security & Kubernetes Deployment

Responsible for the **DevSecOps security layer and deployment**.

Tasks:

* GitLeaks secret scanning
* SonarCloud SAST scanning
* Trivy container scanning
* Kubernetes deployment
* Minikube cluster setup


---

📁 Project Structure
devsecops-project/
│
├── order-service/                  # Order Microservice
│     ├── src/main/java/
│     │     ├── OrderServiceApplication.java
│     │     ├── controller/
│     │     │     └── OrderController.java
│     │     ├── service/
│     │     │     └── OrderService.java
│     │     └── model/
│     │           ├── Order.java
│     │           ├── PaymentRequest.java
│     │           └── PaymentResponse.java
│     ├── src/main/resources/
│     │     └── application.properties
│     ├── Dockerfile
│     └── pom.xml
│
├── payment-service/                # Payment Microservice
│     ├── src/main/java/
│     │     ├── PaymentServiceApplication.java
│     │     ├── controller/
│     │     │     └── PaymentController.java
│     │     ├── service/
│     │     │     └── PaymentService.java
│     │     └── model/
│     │           ├── Payment.java
│     │           └── PaymentRequest.java
│     ├── src/main/resources/
│     │     └── application.properties
│     ├── Dockerfile
│     └── pom.xml
│
├── k8s/                            # Kubernetes Manifests
│     ├── order-deployment.yaml
│     ├── order-service.yaml
│     ├── payment-deployment.yaml
│     └── payment-service.yaml
│
├── .github/
│     └── workflows/
│           └── pipeline.yml        # GitHub Actions CI/CD Pipeline
│
├── .pre-commit-config.yaml         # GitLeaks Pre-commit Hook
├── docker-compose.yml              # Local Development
└── README.md


# ⚙️ Required Tools

Install the following tools before running the project.

 1 Git
2 Java 17
3 Maven
4 Docker


# 🛠 Step 1 — Clone Repository

```id="p7"
git clone https://github.com/rvambadi69/DevSecOps.git
cd DevSecOps
```

---

# 🛠 Step 2 — Build Microservices Using Maven

## Build Order Service

```id="p8"
cd MicroServices/order-service
mvn clean package
```

Generated file:

```id="p9"
target/order-service-1.0.0.jar
```

---

## Build Payment Service

```id="p10"
cd ../payment-service
mvn clean package
```

Generated file:

```id="p11"
target/payment-service-1.0.0.jar
```

---

# 🐳 Step 3 — Build Docker Images

Navigate to project root:

```id="p12"
cd MicroServices
```

Build Order Service image:

```id="p13"
docker build -t rvambadi69/order-service ./order-service
```

Build Payment Service image:

```id="p14"
docker build -t rvambadi69/payment-service ./payment-service
```

Verify images:

```id="p15"
docker images
```

Expected images:

```id="p16"
rvambadi69/order-service
rvambadi69/payment-service
```

---

# 🔐 Step 4 — Docker Hub Login

Login to Docker Hub:

```id="p17"
docker login
```

---

# 📦 Step 5 — Push Images to Docker Hub

Push Order Service image:

```id="p18"
docker push rvambadi69/order-service
```

Push Payment Service image:

```id="p19"
docker push rvambadi69/payment-service
```

Docker Hub repositories:

Order Service

```id="p20"
https://hub.docker.com/r/rvambadi69/order-service
```

Payment Service

```id="p21"
https://hub.docker.com/r/rvambadi69/payment-service
```

---

# ⚙️ Step 6 — Create CI/CD Pipeline

Pipeline file location:

```id="p22"
.github/workflows/pipeline.yml
```

Pipeline triggers when code is pushed.

```id="p23"
on:
  push:
    branches:
      - main
      - ci/cd_engineer
```

---

# ⚙️ Step 7 — Complete Pipeline Configuration
devsecops->.github->workflows->pipeline.yml
  write your code here


# 🔐 Step 8 — Configure GitHub Secrets

Go to:

```
Repository Settings → Secrets → Actions
```

Add the following secrets:

```
DOCKER_USERNAME
DOCKER_PASSWORD
```

These secrets allow GitHub Actions to push images securely.

---

# ▶️ Step 9 — Trigger CI/CD Pipeline

Make a commit:

```id="p25"
git add .
git commit -m "Trigger CI/CD pipeline"
git push origin main
```

---

# 📊 Step 10 — Verify Pipeline Execution

Open:

```
https://github.com/rvambadi69/DevSecOps/actions
```

Expected stages:

```
build   ✔
docker  ✔
```

---

# 🐳 Step 11 — Verify Docker Images

Check Docker Hub:

```
https://hub.docker.com/u/rvambadi69
```

Images should appear:

```
order-service
payment-service
```

---

# ▶️ Step 12 — Run Containers from Docker Hub

Pull images:

```id="p26"
docker pull rvambadi69/order-service
docker pull rvambadi69/payment-service
```

Run containers.

Order Service:

```id="p27"
docker run -p 8080:8080 rvambadi69/order-service
```

Payment Service:

```id="p28"
docker run -p 8081:8081 rvambadi69/payment-service
```

---

# 🔎 Step 13 — Test APIs

Create Order:

```id="p29"
curl -X POST http://localhost:8080/orders \
-H "Content-Type: application/json" \
-d '{"product":"Laptop","amount":999}'
```

Expected response:

```id="p30"
{
 "orderId": "xxxx",
 "product": "Laptop",
 "amount": 999,
 "status": "CONFIRMED"
}
```

Check Orders:

```id="p31"
curl http://localhost:8080/orders
```

Check Payments:

```id="p32"
curl http://localhost:8081/payments
```

---

🔐 Next Steps — Security Implementation (Member 3)

Praveen must integrate:

GitLeaks

Secret scanning

gitleaks detect
SonarCloud

Static code security scanning

Add GitHub Secret:

SONAR_TOKEN
Trivy

Docker vulnerability scanning

trivy image rvambadi69/order-service
☸ Kubernetes Deployment (Next Stage)

Install Minikube:

minikube start

Create Kubernetes files:

k8s/
 ├── order-deployment.yaml
 ├── payment-deployment.yaml
 ├── order-service.yaml
 └── payment-service.yaml

Deploy:

kubectl apply -f k8s/


# 🧩 DevSecOps Pipeline Architecture

```
Developer
   │
   ▼
GitHub Repository
   │
   ▼
GitHub Actions CI/CD
   │
   ├── Build (Maven)
   ├── Docker Build
   └── Docker Push
   │
   ▼
Docker Hub
   │
   ▼
Security Scanning (GitLeaks, SonarCloud, Trivy)
   │
   ▼
Kubernetes Deployment
```

---

# ✅ Work Completed by member 2

Successfully implemented:

* CI/CD pipeline using GitHub Actions
* Automated Maven builds
* Docker containerization
* Docker Hub integration
* Secure credentials using GitHub Secrets
* Pipeline verification
* Docker image publishing

The pipeline is now ready for the **security and Kubernetes deployment stages**.

---
