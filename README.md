# DAMAP Backend Instances

This repository manages DAMAP backend deployments for different institutions using a shared Docker build system.

## Current Institutions
Currently we have the backend images of these institutes.

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


# TODO
* copy the src and pom.xml to the /instance/MUG/ from : https://github.com/sharedRDM/damap-backend-mug
* copy the src and pom.xml to the /instance/TUG/ from : https://github.com/sharedRDM/damap-backend
* `build-backend-mug` will do exactly the same as your local command: `docker build --build-arg INSTANCE_NAME=MUG -t damap-backend-mug`
* find a way to build with ARGS
