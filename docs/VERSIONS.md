# DAMAP Instance Version Management

This describes the version management system for DAMAP instances (TUG, MUG, JKU, etc.).

## Overview

We use a **Parent POM approach** combined with **CI/CD flexibility** to manage `damap-base` versions across all instances.

## Architecture

### Parent POM (`pom.xml`)
- **Central version management** for `damap-base` dependency
- **Instance-specific defaults**:
  - **TUG**: `0.0.0-SNAPSHOT` (follows upstream development)
  - **MUG**: `4.5.2` (stable version)
  - **JKU**: `4.4.0` (stable version)
- **Profiles** for different scenarios:
  - `use-latest-release`: Uses stable released version (4.6.1)
  - `production`: Production-ready version

### Instance POMs (TUG, MUG, JKU)
- **Inherit** from parent POM
- **Can override** base version if needed
- **Keep their own identity** (groupId, artifactId)

### CI/CD Pipeline
- **Supports version override** via build arguments
- **Unified release tagging** for all instances
- **Flexible instance selection** (individual or all)

## Usage scenarios

### Default Behavior (Instance-specific)
```bash
# TUG uses SNAPSHOT, MUG uses 4.5.2, JKU uses 4.4.0
mvn clean package
```

### Use Latest Release
```bash
# Uses stable release version
mvn clean package -Puse-latest-release
```

### Override Version in CI/CD
```bash
# Specify custom version
mvn clean package -Ddamap.base.version=4.7.0
```

### Override in Child POM
```xml
<!-- In instances/TUG/pom.xml -->
<properties>
  <damap.base.version>4.7.0</damap.base.version>
</properties>
```

## CI/CD Features

### Manual Workflow Dispatch Options

1. **Instance Selection**:
   - `TUG` - Build only TUG
   - `MUG` - Build only MUG  
   - `JKU` - Build only JKU
   - `all` - Build all instances

2. **Base Version Override**:
   - Leave empty: Use parent POM default
   - Specify version: Override for this build (e.g., `4.7.0`)

3. **Release Tagging**:
   - Leave empty: Use branch/tag name
   - Specify tag: Tag all images with same version (e.g., `latest`, `v4.7.0`)

### Example Workflows

#### Development Build
```yaml
# Uses SNAPSHOT version from parent POM
- Trigger: Push to main
- Result: Images tagged with "main"
```

#### Testing New Base Version
```yaml
# Manual dispatch with custom base version
- Instance: "all"
- Base Version: "4.7.0"
- Release Tag: "test-4.7.0"
- Result: All images built with base 4.7.0, tagged as "test-4.7.0"
```

#### Official Release
```yaml
# Manual dispatch for official release
- Instance: "all"
- Base Version: "4.6.1" (stable)
- Release Tag: "latest"
- Result: All images built with stable base, tagged as "latest"
```
