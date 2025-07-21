# Development Setup

This guide helps you set up a local development environment for DAMAP instances.

## System Requirements

For backend development: Java 17+, Maven 3.8+, PostgreSQL 13+ (if not using Docker). For frontend development: Node.js 18+, npm 9+.

## Quick Setup with Docker Compose

The easiest way to get started is using Docker Compose. Clone the repositories and start the services:

```bash
git clone https://github.com/sharedRDM/damap-instance.git
git clone https://github.com/tugraz-rdm/damap-frontend.git

# Build TUG instance locally first
cd damap-instance/instances/TUG
mvn clean install -DskipTests

# Start services with Docker
cd ../../../damap-backend/docker
docker compose up --build --force-recreate -d
```

Access the system:
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/q/swagger-ui/
- Keycloak: http://localhost:8087 (admin/admin)

Login credentials: user/user

## Local Development

For local development without Docker:

Backend:
```bash
cd damap-instance/instances/TUG
mvn clean install -DskipTests
mvn quarkus:dev
```

Frontend:
```bash
cd damap-frontend
npm install
npm start
```

## Testing Changes

```bash
# Backend
mvn clean compile
mvn test

# Frontend
npm run build
npm run test
```

## Common Issues

**Database connection fails**: Check PostgreSQL is running and verify credentials in `application.yaml`.

**Authentication issues**: Ensure Keycloak is running with correct configuration.

**Frontend can't connect**: Verify API URLs point to correct backend address.

**Docker errors**: Reset containers:
```bash
docker compose down -v
docker compose up --build -d
``` 