# DAMAP Backend Instances

This repository manages DAMAP backend deployments for different institutions using a shared Docker build system.

## Current Institutions

- **TUG** - Graz University of Technology (Backend)
- **MUG** - Medical University of Graz (Backend)

## Structure

```
instances/
├── MUG/
│   ├── src/
│   └── pom.xml
└── TUG/
    ├── src/
    └── pom.xml
```

## How to use

### Build Docker images

Build the Docker image for any backend instance:

```bash
# Build MUG backend version
docker build --build-arg INSTANCE_NAME=MUG -t damap-backend-mug .

# Build TUG backend version  
docker build --build-arg INSTANCE_NAME=TUG -t damap-backend-tug .
```

