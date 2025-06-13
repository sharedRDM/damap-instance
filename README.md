# DAMAP Backend Instances

This repository manages DAMAP backend deployments for different institutions using a shared Docker build system.

## Current Institutions

- **TUG** - Graz University of Technology
- **MUG** - Medical University of Graz

## Structure

```
instances/
├── TUG/
│   ├── src/                    # TUG-specific source code  
│   │   ├── main/java/at/tugraz/damap/...
│   │   └── main/resources/...
│   └── pom.xml                 # TUG Maven configuration
├── MUG/
│   ├── src/                    # MUG-specific source code
│   │   ├── main/java/at/medunigraz/damap/...
│   │   └── main/resources/...
│   └── pom.xml                 # MUG Maven configuration
```

## Usage

### Building Docker Images Locally

**On Linux (AMD64):**
```bash
# Build TUG backend  
docker build --build-arg INSTANCE_NAME=TUG -t damap-backend-tug .

# Build MUG backend
docker build --build-arg INSTANCE_NAME=MUG -t damap-backend-mug .
```

**On Mac (ARM64/M1/M2):**
```bash
# Build TUG backend  
docker build --platform linux/amd64 --build-arg INSTANCE_NAME=TUG -t damap-backend-tug .

# Build MUG backend
docker build --platform linux/amd64 --build-arg INSTANCE_NAME=MUG -t damap-backend-mug .
```

