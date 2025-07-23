# DAMAP Backend Instances

This repository manages DAMAP backend deployments for different institutions using a shared Docker build system.

## Documentation

For detailed guides and migration instructions, see [**docs/**](docs/) directory:

## Current Institutions

- **TUG** - Graz University of Technology
- **MUG** - Medical University of Graz
- **JKU** - Johannes Kepler University Linz

## Structure

```
instances/
├── TUG/
│   ├── src/                    
│   │   ├── main/java/at/tugraz/damap/...
│   │   └── main/resources/...
│   └── pom.xml                 
├── MUG/
│   ├── src/                    
│   │   ├── main/java/at/medunigraz/damap/...
│   │   └── main/resources/...
│   └── pom.xml                
├── JKU/
│   ├── src/                   
│   │   ├── main/java/at/jku/damap/...
│   │   └── main/resources/...
│   └── pom.xml                 
```

## Usage

### Building Docker Images Locally

**On Linux (AMD64):**
```bash
# Build TUG backend  
docker build --build-arg INSTANCE_NAME=TUG -t damap-backend-tug .

# Build MUG backend
docker build --build-arg INSTANCE_NAME=MUG -t damap-backend-mug .

# Build JKU backend
docker build --build-arg INSTANCE_NAME=JKU -t damap-backend-jku .
```

**On Mac (ARM64/M1/M2):**
```bash
# Build TUG backend  
docker build --platform linux/amd64 --build-arg INSTANCE_NAME=TUG -t damap-backend-tug .

# Build MUG backend
docker build --platform linux/amd64 --build-arg INSTANCE_NAME=MUG -t damap-backend-mug .

# Build JKU backend
docker build --platform linux/amd64 --build-arg INSTANCE_NAME=JKU -t damap-backend-jku .
```

# Test build - Wed Jul 23 16:01:47 CEST 2025
