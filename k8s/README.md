## Kubernetes manifests for DevSecOps demo

This folder contains Kubernetes manifests for the demo microservices (Order Service and Payment Service).

This README explains what each file does, how to deploy the demo to a local Kubernetes cluster (Minikube / kind), important runtime notes (health probes, image pull policy, ports), and quick troubleshooting tips.

### Files in this directory

- `order-deployment.yaml` — Deployment for the Order Service
- `order-service.yaml` — Service (NodePort) exposing Order Service
- `payment-deployment.yaml` — Deployment for the Payment Service
- `payment-service.yaml` — ClusterIP Service for Payment Service

---

## Overview

The two microservices are:

- Order Service — listens on port 8080. Receives order creation requests, calls Payment Service, holds orders in memory.
- Payment Service — listens on port 8081. Simulates payment processing (80% success), holds payments in memory.

The Kubernetes manifests are designed for local/demo clusters (Minikube or kind). The `order-deployment.yaml` uses `imagePullPolicy: Never` which assumes the image is available locally on the node (useful when building images on your machine and running them in Minikube).

## How to deploy (Minikube)

1. Start Minikube:

```bash
minikube start
```

2. Build Docker images and load them into Minikube (option A), or push to a registry (option B).

Option A — build locally and use with imagePullPolicy: Never or load into minikube:

```bash
# from repo root
cd MicroServices
docker build -t order-service:latest ./order-service
docker build -t payment-service:latest ./payment-service

# Load into minikube so cluster nodes can use local images (alternative to imagePullPolicy: Never)
minikube image load order-service:latest
minikube image load payment-service:latest
```

Option B — push images to a registry and update manifests to reference the registry-tagged images (recommended for CI/CD/production):

```bash
docker build -t yourdockerhubuser/order-service:1.0.0 ./MicroServices/order-service
docker push yourdockerhubuser/order-service:1.0.0
# update k8s manifest to use yourdockerhubuser/order-service:1.0.0 and set imagePullPolicy: IfNotPresent or Always
```

3. Apply manifests (deploy Payment service first, then Order service):

```bash
kubectl apply -f k8s/payment-deployment.yaml
kubectl apply -f k8s/payment-service.yaml

kubectl apply -f k8s/order-deployment.yaml
kubectl apply -f k8s/order-service.yaml
```

4. Verify pods and services:

```bash
kubectl get pods
kubectl get svc

# For NodePort (order-service uses nodePort 30080 in the provided manifest)
minikube service order-service --url
```

---

## Important notes about the manifests

- imagePullPolicy: `Never` (in `order-deployment.yaml`) — intended for local testing when the image is present on the node. For CI or real clusters, push to a registry and change to `IfNotPresent` or `Always`.

- Replicas: the `order-deployment.yaml` uses `replicas: 1`. Increase `replicas` for high availability in non-demo environments.

- Ports and Services:
  - Order Service containerPort: `8080`. The `order-service.yaml` exposes port 8080 and maps it to NodePort `30080`.
  - Payment Service containerPort: `8081`. `payment-service.yaml` is a `ClusterIP` service so other pods (like Order) can reach it via `http://payment-service:8081`.

- Health probes:
  - The deployments use readiness and liveness probes that call `GET /actuator/health` on port `8080` (Order) and similar for Payment. These probes require Spring Boot Actuator to be enabled and exposed at `/actuator/health` (check `MicroServices/*/src/main/resources/application.properties`). If Actuator is not enabled, the probes will fail and the Pod will be marked unready or restarted.

  - Probe tuning in the current manifests:
    - readinessProbe: initialDelaySeconds: 30, periodSeconds: 10
    - livenessProbe: initialDelaySeconds: 60, periodSeconds: 10

  Adjust these depending on the actual application startup time.

- Resources:
  - Example resource requests/limits exist in the `order-deployment.yaml` (requests: 256Mi/250m, limits: 512Mi/500m). Tune these according to your load and measurements.

---

## Troubleshooting

- Pod fails to start with image errors:
  - If using `imagePullPolicy: Never`, ensure the image exists on the node. Use `minikube image load` or push to a registry and update the manifest.

- Pod stuck NotReady or not receiving traffic:
  - Check readinessProbe and livenessProbe; make sure the health endpoint exists. Example:

```bash
kubectl describe pod <pod-name>
kubectl logs <pod-name>
```

- Service unreachable from outside (NodePort):
  - Confirm the `order-service` manifest uses `type: NodePort` and `nodePort: 30080`. Use `minikube service order-service --url` or `minikube tunnel` to access services.

- Inter-service calls failing (Order → Payment):
  - Inside cluster the Order Service calls `http://payment-service:8081/payments`. Ensure the `payment-service` Service exists and selector labels match the Payment deployment's pod labels.

