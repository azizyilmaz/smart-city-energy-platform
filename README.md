# Smart City Energy Platform – Core Infrastructure

This repository contains the **core Spring Cloud infrastructure** for the *Smart City Energy Distribution & Billing Platform*.

At this stage, the focus is on establishing a **production-ready foundation** using modern Spring Cloud components **without Netflix OSS**.

---

## Table of Contents

- [Implemented Components](#-implemented-components)
- [Project Structure](#-project-structure)
- [Technology Stack](#-technology-stack)
- [Infrastructure (Docker)](#-infrastructure-docker)
- [Application Startup Order](#-application-startup-order)
- [Verification & Health Checks](#-verification--health-checks)
- [Architectural Notes](#-architectural-notes)
- [Next Steps](#-next-steps)
- [Notes](#-notes)
- [Contact](#contact)

---

## ✨ Implemented Components

### 1. Spring Cloud Config Server
- Centralized external configuration management
- Native file-based configuration repository
- Registered with Consul for service discovery
- Ready for Spring Cloud Bus integration

### 2. HashiCorp Consul (Service Discovery)
- Used as the service registry
- Runs as external infrastructure via Docker
- Enables client-side discovery and load balancing

### 3. Spring Cloud Gateway
- Single entry point for all downstream services
- Integrated with:
    - Config Server (centralized config)
    - Consul Discovery
    - Spring Cloud LoadBalancer
- Supports routing, predicates, and filters

### 4. Dummy Meter Service
- Simulates **smart meter devices** producing consumption data
- Registers itself with Consul
- Exposes REST endpoints for synchronous access
- Produces **MeterReadingEvent** messages to RabbitMQ
- Acts as the primary **data source** for the platform

---

### 5. Feign Client Integration
- Downstream services access `meter-service` via **Feign Client**
- Uses **Consul-based service discovery**
- Client-side load balancing handled by **Spring Cloud LoadBalancer**
- No hardcoded hostnames or ports
- Demonstrates **service-to-service communication**

---

### 6. Resilience4J – Circuit Breaker
- Fault tolerance added on Feign Client calls
- Circuit Breaker patterns applied:
    - Failure rate threshold
    - Slow call detection
    - Automatic fallback handling
- Prevents cascading failures when `meter-service` is unavailable
- Fully integrated with Spring Boot Actuator

---

### 7. RabbitMQ + Spring Cloud Stream
- Asynchronous event-driven communication enabled
- Uses **Spring Cloud Function programming model**
- Meter readings are published as domain events
- Analytics services consume events via typed Consumers
- RabbitMQ runs as infrastructure via Docker
- Ensures loose coupling between producers and consumers

---

## 🏗️ Project Structure

* smart-city-infra/
* │
* ├── config-server/
* │ ├── src/
* │ │ └── main/java/com/smartcity/configserver
* │ ├── config-repo/
* │ │ └── api-gateway.yml
* │ │ ├── meter-service.yml
* │ │ └── analytics-service.yml
* │ ├── pom.xml
* │ └── application.yml
* │
* ├── api-gateway/
* │ ├── src/
* │ │ └── main/java/com/smartcity/apigateway
* │ ├── pom.xml
* │ └── application.yml
* │
* ├── meter-service/
* │ ├── src/
* │ │ └── main/java/com/smartcity/meter
* │ │ ├── controller
* │ │ ├── service
* │ │ ├── event
* │ │ └── stream
* │ ├── pom.xml
* │ └── application.yml
* │
* ├── analytics-service/
* │ ├── src/
* │ │ └── main/java/com/smartcity/analytics
* │ │ ├── client
* │ │ ├── event
* │ │ ├── service
* │ │ └── stream
* │ ├── pom.xml
* │ └── application.yml
* │
* ├── docker/
* │ └── docker-compose.yml
* │
* └── README.md


This structure reflects a **clear separation of concerns**:

- `config-server`  
  Centralized configuration management for all services.

- `api-gateway`  
  Single external entry point, responsible for routing, filtering, and load-balanced access.

- `meter-service`  
  Simulates smart meters, exposes REST endpoints, and publishes meter reading events.

- `analytics-service`  
  Consumes meter events asynchronously, calls Meter Service synchronously using Feign, and applies circuit breaking.

- `docker`  
  Contains only shared infrastructure components (Consul, RabbitMQ).

---

## 🔧 Technology Stack

- **Java 21**
- **Spring Boot 3.x**
- **Spring Cloud 2024.x**
- **Spring Cloud Config Server**
- **Spring Cloud Gateway**
- **Spring Cloud Consul Discovery**
- **Spring Cloud LoadBalancer**
- **Spring Cloud OpenFeign**
- **Resilience4J**
- **Spring Cloud Stream**
- **RabbitMQ**
- **Docker & Docker Compose**

---

## 🐳 Infrastructure (Docker)

Only infrastructure components run in Docker.

### Start Consul
```bash
cd docker
docker compose up -d
```
This will start:
- **Consul** – Service Discovery
- **RabbitMQ** – Event Broker

---

### Consul UI

Once Consul is running, you can access the web UI at:

```bash
http://localhost:8500
```

From the Consul UI you can:
- Verify that services are registered
- Check service health status
- Inspect service metadata and instances
- Validate client-side load balancing behavior

---

### RabbitMQ Management UI

RabbitMQ provides a management console to inspect exchanges, queues, and message flow within the platform.

```bash
http://localhost:15672
```

Default credentials:

```text
username: guest
password: guest
```

Using the RabbitMQ Management UI you can:

- Verify that meter-service publishes events correctly
- Inspect exchanges and bindings created by Spring Cloud Stream
- Observe queues used by analytics-service
- Monitor message rates, acknowledgements, and consumer activity

## ▶️ Application Startup Order

> ⚠️ **Startup order is critical for correct behavior**

Follow the steps below exactly in this order.

### 1. Start Infrastructure
```bash
docker compose up -d
```

Ensure that:
- Consul is accessible on port 8500
- RabbitMQ Management UI is accessible on port 15672

Do not start any Spring Boot applications before this step.

---

### 2. Start Config Server

Run the following application from your IDE:

config-server → ConfigServerApplication

Expected behavior:
- Config Server starts on port **8888**
- Registers itself with Consul
- Loads configuration from the local `config-repo` directory

You can verify that the Config Server is running by accessing:

```bash
http://localhost:8888/actuator/health
```

---

### 3. Start API Gateway

Run the following application from your IDE:

api-gateway → ApiGatewayApplication

Expected behavior:
- API Gateway starts on port **8080**
- Fetches configuration from the Config Server
- Registers itself with Consul
- Initializes Spring Cloud Gateway routes

---

### 4. Start Meter Service

Run the following application from your IDE:

meter-service → MeterServiceApplication


Expected behavior:
- Registers with Consul
- Exposes REST endpoints for meter data
- Publishes `MeterReadingEvent` messages to RabbitMQ

---

### 5. Start Analytics Service

Run the following application from your IDE:

analytics-service → AnalyticsServiceApplication

Expected behavior:
- Registers with Consul
- Consumes meter events asynchronously via Spring Cloud Stream
- Calls Meter Service synchronously using Feign Client
- Protected by Resilience4J Circuit Breaker

---

## 🔍 Verification & Health Checks

### 1. Config Server Verification

Verify that the API Gateway configuration is successfully served by the Config Server:

```bash
http://localhost:8888/api-gateway/default
```

Expected result:
- The `propertySources` array is **not empty**
- Configuration is loaded from:

file:./config-repo/api-gateway.yml


If `propertySources` is empty, check:
- `spring.cloud.config.server.native.search-locations`
- Placement of the `config-repo` directory
- That the Config Server was restarted after configuration changes

---

### 2. API Gateway Routing Test

The API Gateway exposes a test route that forwards requests to **httpbin.org**.

Send a request to:

```bash
http://localhost:8080/test/get
```

Expected behavior:
- Request is received by Spring Cloud Gateway
- `/test` path prefix is removed using the `StripPrefix` filter
- The request is forwarded to:

```bash
https://httpbin.org/get
```

If the response returns `404 Not Found`, verify:
- Gateway route definition
- `StripPrefix` filter configuration
- That the Gateway application was restarted after changes

---

### 3. Service Discovery Verification (Consul)

Open the Consul Web UI:

```bash
http://localhost:8500
```

Under the **Services** section, the following services should be visible:
- `config-server`
- `api-gateway`

Both services should:
- Be registered successfully
- Have a **healthy** status

---

### 4. Gateway → Meter Service Routing

```bash
http://localhost:8080/meters/api/readings
```

Expected:
- Request routed via Gateway
- Load-balanced via Consul
- Response from Meter Service

---

### 5. Feign Client Verification

- Analytics service calls Meter service using Feign
- No hardcoded hostnames
- Calls resolved via Consul + LoadBalancer

---

### 6. Circuit Breaker Verification

- Stop Meter Service
- Trigger Feign call
- Observe fallback logic
- Circuit transitions:
  - CLOSED → OPEN → HALF_OPEN

---

### 7. RabbitMQ Event Flow Verification

- Open RabbitMQ UI
- Verify:
  - Exchange: meter-events
  - Queue: meter-events.analytics-group
- Publish meter events
- Observe Analytics service logs

---

## 🧠 Architectural Notes

- Synchronous + Asynchronous communication combined
- REST (Feign) for queries
- Events (RabbitMQ) for high-volume data
- Circuit Breaker prevents cascading failures
- Consul is used as the service registry instead of Netflix Eureka
- Spring Cloud LoadBalancer is enabled automatically through discovery
- Config Server acts as the centralized source of configuration
- API Gateway is the single external entry point to the system
- Applications run locally via IDE for fast development feedback
- Docker is used strictly for shared infrastructure components

---

## 🚀 Next Steps

Planned next steps for this platform include:
- Dynamic pricing engine
- Spring Cloud Bus for config refresh
- Micrometer + Zipkin distributed tracing
- Spring Cloud Data Flow pipelines
- Deployment to Pivotal Cloud Foundry

---

## 📌 Notes

This setup intentionally reflects:
- Real-world enterprise Spring Cloud projects
- Event-driven + request-driven hybrid architecture
- Clean separation between infrastructure and applications
- A scalable foundation for future microservices

---

## Contact

**Aziz Yılmaz**

- Software Developer / Application Architect
- Focus areas:
    - Spring Boot & Spring Cloud
    - Event-Driven & Microservices Architecture
    - Cloud-Native Systems

📧 Email: *azizxyilmaz@outlook.com*  
💼 LinkedIn: [Aziz Yılmaz](https://www.linkedin.com/in/aziz-yilmaz)  
🐙 GitHub: [azizyilmaz](https://github.com/azizyilmaz)

This project is maintained as a **reference architecture** and a **learning-oriented enterprise-grade Spring Cloud platform**.
