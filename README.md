# File Management System

A microservice-based File Management System built with Spring Boot, designed for secure file upload, processing, and management with role-based authentication and authorization.

## 🏗️ Architecture

This project follows a microservice architecture pattern with the following components:

                            ┌───────────────────┐
                            │   React Frontend  │
                            │  (Next.js + Redux)│
                            └────────┬──────────┘
                                     │
                                     ▼
                            ┌───────────────────┐
                            │   API Gateway     │
                            │   (Proxy Service) │
                            └────────┬──────────┘
                                     │
            ┌────────────────────────┼────────────────────────┐
            ▼                        ▼                        ▼
    ┌────────────────┐       ┌─────────────────┐        ┌────────────────┐
    │ User/Auth      │       │ File Management │        │ Logging        │
    │ Service        │       │ Service         │        │ Service        │
    └───────┬────────┘       └────────┬────────┘        └───────┬────────┘
            │                        │                        │
            ▼                        ▼                        ▼
        MySQL                   MongoDB                  InfluxDB
                                                          (Phase 2)





All services communicate through the **API Gateway**, which acts as a single entry point for the frontend. The frontend (React/Next.js) only talks to the API Gateway, which then routes requests to the appropriate backend microservice.

## 🌿 Branches

| Branch | Purpose |
|--------|---------|
| `main` | Production-ready code. No credentials or sensitive configs included. |
| `test` | Contains test credentials, RSA keys, DB configs, etc. for quick local setup and testing. **Use this branch to get started quickly.** |

> ⚠️ **Note:** The `test` branch is intended only for local development/testing purposes. Do not use test credentials or keys in any production environment.

## 🧩 Microservices

### 1. User/Auth Service
- Handles user registration, login, and profile management
- Authentication using Spring Security
- Authorization via JWT tokens
- JWT signed using **RSA private key**, verified using **RSA public key**
- User data stored in MySQL
- *Planned (Phase 2):* Migrate to Okta for identity and access management

### 2. File Management Service
- Handles file upload, storage, and processing
- Uses **Spring Batch** for file processing jobs
- Currently supports `.xlsx` files only
- File metadata stored in MongoDB
- Redis integrated for faster access to file information
- *Planned (Phase 2):* RAG-based system with semantic search and LLM integration for intelligent file analysis

### 3. Logging Service
- Centralized logging for all microservices
- *Planned (Phase 2):* InfluxDB integration for time-series log storage and analytics

### API Gateway
- Single entry point for all client requests
- Routes requests to appropriate downstream microservices
- Acts as a proxy layer between frontend and backend services

## 🛠️ Tech Stack

### Backend
- Java, Spring Boot
- Spring Security (Authentication & Authorization)
- Spring Batch (File Processing)
- Spring Data JPA
- Lombok
- Swagger / OpenAPI (API Documentation)

### Frontend
- React.js
- Next.js
- Redux
- Tailwind CSS

### Database
- MySQL – User/Auth data
- MongoDB – File management data
- InfluxDB – Logging data *(Phase 2)*

### DevOps
- Docker & Docker Compose
- Git

### Caching
- Redis – used for faster access to file information and authenticated user session/details

## 🔐 Security

- **Authentication:** Managed via Spring Security, user credentials stored securely in MySQL
- **Authorization:** JWT-based, tokens signed with RSA private key and verified with RSA public key
- **Global Exception Handling:** Centralized exception handling across services for consistent error responses
- *Planned (Phase 2):* Replace custom JWT/RSA setup with Okta for identity management

## 🚀 Getting Started

### Prerequisites
- Docker & Docker Compose installed
- Git

### Setup Instructions

1. Clone the repository and switch to the `test` branch for quick local setup (includes test credentials, RSA keys, and DB configs):
```bash
   git clone <repository-url>
   cd <project-folder>
   git checkout test
```

2. Build and run all services using Docker Compose:
```bash
   docker compose up --build
```

3. Once all containers are up and running, open your browser and navigate to:


4. You should now be able to access the File Management System frontend and start testing.

> For production use, switch to the `main` branch and configure your own environment variables, RSA keys, and database credentials.

## 📄 File Processing

- Files are processed using **Spring Batch** jobs
- Currently, only `.xlsx` files are supported for upload and processing
- Processed file metadata is cached in Redis for quick retrieval

## 🗺️ Roadmap (Phase 2)

- [ ] Integrate InfluxDB for logging service
- [ ] Replace custom JWT/RSA authentication with Okta
- [ ] Build RAG (Retrieval-Augmented Generation) system for file analysis
- [ ] Enable semantic search over uploaded files
- [ ] Integrate LLM for intelligent file analysis and Q&A
- [ ] Support additional file formats beyond `.xlsx`

## 📚 API Documentation

Each service exposes its own Swagger UI for API documentation and testing. Once services are running, refer to individual service endpoints (via the API Gateway) for detailed API specs.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature-name`)
3. Commit your changes
4. Push to the branch and open a Pull Request

## 📝 License

Add your license information here.

## 📧 Contact

Add your contact/support information here.