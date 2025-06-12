# DAMAP Instances

This repository manages DAMAP deployments for different institutions using a shared Docker build system.

## Current Institutions

- **TUG** - Graz University of Technology (Backend)
- **MUG** - Medical University of Graz (Backend)

## Structure

```
instances/
├── backend/
│   ├── MUG/
│   │   ├── src/
│   │   └── pom.xml
│   └── TUG/
│       ├── src/
│       └── pom.xml
└── frontend/          
    ├── MUG/           # (planned)
    └── TUG/           # (planned)
```

## How to use

### Extract backend source code

Extract all backend instances:
```bash
./scripts/extract-instances.sh backend
```

Extract specific backend instance:
```bash
./scripts/extract-instances.sh backend MUG
./scripts/extract-instances.sh backend TUG
```

### Build Docker images (Backend)

After extracting, build the Docker image for any backend instance:

```bash
# Build MUG backend version
docker build --build-arg INSTANCE_TYPE=backend --build-arg INSTANCE_NAME=MUG -t damap-backend-mug .

# Build TUG backend version  
docker build --build-arg INSTANCE_TYPE=backend --build-arg INSTANCE_NAME=TUG -t damap-backend-tug .
```

