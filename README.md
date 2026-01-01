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

---

## 🏗️ Project Structure

* smart-city-infra/
* │
* ├── config-server/
* │ ├── src/
* │ ├── config-repo/
* │ │ └── api-gateway.yml
* │ ├── pom.xml
* │ └── application.yml
* │
* ├── api-gateway/
* │ ├── src/
* │ ├── pom.xml
* │ └── application.yml
* │
* ├── docker/
* │ └── docker-compose.yml
* │
* └── README.md


---

## 🔧 Technology Stack

- **Java 21**
- **Spring Boot 4.x**
- **Spring Cloud 2025.x**
- **Spring Cloud Config Server**
- **Spring Cloud Gateway**
- **Spring Cloud Consul Discovery**
- **Spring Cloud LoadBalancer**
- **Docker & Docker Compose**

---

## 🐳 Infrastructure (Docker)

Only infrastructure components run in Docker.

### Start Consul
```bash
cd docker
docker compose up -d
```

### Consul UI

Once Consul is running, you can access the web UI at:

```bash
http://localhost:8500
```

From the Consul UI you can:
- Verify that services are registered
- Check service health status
- Inspect service metadata and instances

---

## ▶️ Application Startup Order

> ⚠️ **Startup order is critical for correct behavior**

Follow the steps below exactly in this order.

### 1. Start Infrastructure
```bash
docker compose up -d
```

Ensure Consul is up and accessible before starting any Spring Boot application.

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

## 🧠 Architectural Notes

- Consul is used as the service registry instead of Netflix Eureka
- Spring Cloud LoadBalancer is enabled automatically through discovery
- Config Server acts as the centralized source of configuration
- API Gateway is the single external entry point to the system
- Applications run locally via IDE for fast development feedback
- Docker is used strictly for shared infrastructure components

---

## 🚀 Next Steps

Planned next steps for this platform include:
- Adding a backend service behind the Gateway
- Introducing Feign Clients with Consul-based load balancing
- Applying Resilience4J for fault tolerance
- Integrating RabbitMQ with Spring Cloud Stream
- Enabling distributed tracing with Micrometer and Zipkin

Each component will be integrated incrementally on top of this foundation.

---

## 📌 Notes

This setup intentionally reflects:
- Real-world enterprise Spring Cloud projects
- Modern, non-Netflix OSS architecture
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
