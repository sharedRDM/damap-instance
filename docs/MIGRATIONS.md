# DAMAP Migration Guide

This guide provides step-by-step instructions for migrating DAMAP instances to new versions.

## Quick Reference

| From Version | To Version | Breaking Changes | Required Actions |
|--------------|------------|------------------|------------------|
| 4.6.0        | 4.6.1      | ORCID service class path | [Update ORCID class path](#v461-migration) |
| 4.3.0        | 4.5.2      | Multiple changes | [See legacy migration](#legacy-migrations) |

## TU Graz Custom Files

During any migration, you should pay special attention to the TU Graz-specific files that integrate with the institutional APIs and systems. The authentication and API integration files include `TUGrazPersonServiceImpl.java` and `TUGrazProjectServiceImpl.java`, which handle the connection to TU Graz's person and project data systems. The `CredentialsService.java` file manages authentication with the TU Graz API endpoints.

The data mapping components consist of `TUGrazPersonDOMapper.java` and `TUGrazProjectDOMapper.java`, which transform data between the TU Graz API format and DAMAP's internal format. These work together with the data model files `TUGrazPerson.java` and `TUGrazProject.java` that define the structure of institutional data.

For document generation, the `TUGrazTemplateFileBrokerServiceImpl.java` file handles custom document templates that may include TU Graz-specific formatting or content requirements.

## Version 4.6.1 Migration

### Prerequisites

- DAMAP backend locally built (for dependency resolution)

### Step 1: Update Version

Update your instance `pom.xml`:

```xml
<groupId>at.tugraz</groupId>
<artifactId>damap</artifactId>
<version>4.6.1</version>  <!-- Changed from 4.6.0 -->
```

### Step 2: Update Maven Plugins

Replace the publishing plugin in your `pom.xml`:

```xml
<!-- REMOVE this old plugin -->
<plugin>
  <groupId>org.sonatype.plugins</groupId>
  <artifactId>nexus-staging-maven-plugin</artifactId>
  <version>1.7.0</version>
  <!-- ... -->
</plugin>

<!-- ADD this new plugin -->
<plugin>
  <groupId>org.sonatype.central</groupId>
  <artifactId>central-publishing-maven-plugin</artifactId>
  <version>0.8.0</version>
  <extensions>true</extensions>
  <configuration>
    <publishingServerId>central</publishingServerId>
    <autoPublish>true</autoPublish>
    <waitUntil>published</waitUntil>
  </configuration>
</plugin>
```

### Step 3: Fix ORCID Service Class Path

**Breaking Change**: The ORCID service moved to a new package.

In your `application.yaml`, update:

```yaml
person-services:
  - display-text: "ORCID"
    query-value: "ORCID"
    class-name: "org.damap.base.integration.orcid.ORCIDPersonServiceImpl"  # Updated path
```

**Old path** (will break): `org.damap.base.rest.persons.orcid.ORCIDPersonServiceImpl`  
**New path** (required): `org.damap.base.integration.orcid.ORCIDPersonServiceImpl`

### Step 4: Build Dependencies

Build the DAMAP base dependency first:

```bash
cd ../damap-backend
mvn clean install -DskipTests
```

### Step 5: Test Your Instance

```bash
cd ../damap-instance/instances/TUG  # or MUG/JKU
mvn clean install -DskipTests
```

## Legacy Migrations

### Version 4.3.0 → 4.5.2 (Historical)

This migration introduced several significant changes to the DAMAP base. The most important additions were two new user roles: "Principal Investigator" and "Project Coordinator", which required updates to the role mapping system. The document templates also received major updates, particularly for Horizon Europe and FWF template formats. Additionally, a new "Technical Resource" field was added to the data model, and several Oracle database compatibility issues were resolved.

To complete this migration, you should first update the base dependency version in your `pom.xml` file. Next, thoroughly test all TU Graz custom authentication files to ensure they work with the new base version. The role mappings in both person and project mappers need updates to handle the new roles properly. You should also verify that template generation continues to work correctly with the updated template system. Finally, test all API endpoints to ensure the integration with TU Graz systems remains functional.

The custom files that require specific updates include `TUGrazPersonDOMapper.java` which needs the new role mappings, `TUGrazProjectDOMapper.java` which must handle the new Technical Resource field, and `TUGrazTemplateFileBrokerServiceImpl.java` which should be updated for the new template formats.

## General Migration Process

### 1. Update Base Dependency

In your `pom.xml`:
```xml
<dependency>
  <groupId>org.damap</groupId>
  <artifactId>base</artifactId>
  <version>NEW_VERSION</version>  <!-- Update this -->
</dependency>
```

### 2. Build and Test

```bash
# Clean build
mvn clean compile

# Check dependencies
mvn dependency:tree -Dverbose

# Full test
mvn clean install -DskipTests
```

### 3. Verify TU Graz Integration

After completing the build process, you should thoroughly test the integration with TU Graz systems. Start by testing authentication with the TU Graz APIs to ensure the credentials service still works correctly with any authentication changes in the new version. Next, verify that person and project data mapping continues to function properly by testing data retrieval and transformation from the TU Graz APIs. You should also check that custom template generation produces the expected documents with correct formatting. Finally, test all API endpoints to confirm that the entire integration chain from API calls through data mapping to final output works as expected.

