# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] -

## [4.6.1] - 2025-07-21

### Changed

- Updated ORCID service class path from `org.damap.base.rest.persons.orcid.ORCIDPersonServiceImpl` to `org.damap.base.integration.orcid.ORCIDPersonServiceImpl`
- Updated Maven publishing plugin from `nexus-staging-maven-plugin` to `central-publishing-maven-plugin`
- Disabled JAXB and JSON schema generation plugins for instances (using base dependency instead)

### Added

- Added comprehensive migration documentation in `docs/MIGRATIONS.md`
- Added development setup guide in `docs/DEVELOPMENT.md`
- Added documentation structure following best practices

## [4.6.0] - 2025-07-08

### Added

- Added values for "no license" and "custom license" to license dataset [#373](https://github.com/damap-org/damap-backend/pull/373)
- Added a startup healthcheck to CI pipeline [#364](https://github.com/damap-org/damap-backend/pull/364)
- Added data transfer agreement to options of legal restrictions [#360](https://github.com/damap-org/damap-backend/pull/360)
- Added a javadoc check to mvn verify to insure correct documentation [#397](https://github.com/damap-org/damap-backend/pull/397)
- Added support for marking external storages as managed internally by an institution [#355](https://github.com/damap-org/damap-backend/pull/355)
- Added support for granting and revoking owner rights, editors now cannot create more editor [#399](https://github.com/damap-org/damap-backend/pull/399)
- Added new endpoint for updating the ORCID affiliation of contributors [#381](https://github.com/damap-org/damap-backend/pull/381)

### Changed

- Fixed container permissions for OpenShift deployment [#385](https://github.com/damap-org/damap-backend/pull/385)
- Update Lombok dependency to 1.18.30 for developers on macOS [#367](https://github.com/damap-org/damap-backend/pull/367)
- Added support for assigning contributors multiple roles [#339](https://github.com/damap-org/damap-backend/pull/339)
- Created single docker-compose files for oracle and postgres to make deployment easier [#376](https://github.com/damap-org/damap-backend/pull/376)
- Removed misleading warning logs [#375](https://github.com/damap-org/damap-backend/pull/375)
- Refactored how project and person service integrations are managed (backwards compatibility is still provided) [#398](https://github.com/damap-org/damap-backend/pull/398)
- Replaced mock person ORCIDs with ISNIs to avoid clashes with ORCID API calls [#402](https://github.com/damap-org/damap-backend/pull/402)

### Removed

- Cleaned up CI pipeline by removing SonarCube [#383](https://github.com/damap-org/damap-backend/pull/383)

### Fixed

- Test suite is now independent of R3Data API being available [#382](https://github.com/damap-org/damap-backend/pull/382)
- Fixed bug in exported DMP filename generation [#352](https://github.com/damap-org/damap-backend/pull/352)

### Template

- Added \[contact\] placeholder to FWF template to fill in "Co-ordination of data management responsibilities across partners:" in section I.2 [#356](https://github.com/damap-org/damap-backend/pull/356)
- Changed table settings in Word templates so that they do not split over two pages [#396](https://github.com/damap-org/damap-backend/pull/396)

## [4.5.2] - 2025-04-15

### Fixed

- Fixed a bug which prevented DAMAP from working with an oracle database [#362](https://github.com/tuwien-csd/damap-backend/pull/362).

## [4.5.1] - 2025-04-14

### Fixed

- Fixed a bug which prevented DAMAP from working with an oracle database [#358](https://github.com/tuwien-csd/damap-backend/pull/358).
- Fixed a bug where placeholders were left in the exported document when no project was chosen [#354](https://github.com/tuwien-csd/damap-backend/pull/354).

### Template

- SE relevant policies and guidelines section: Updated broken link to ethics and data protection document [#353](https://github.com/tuwien-csd/damap-backend/pull/353).

## [4.5.0] - 2025-04-01

### Added

- Added the "Technical Resource" field to dataset, which describes the hardware used to capture the dataset [#337](https://github.com/tuwien-csd/damap-backend/pull/337).

### Changed

- Work was done to make the code more bug resistant [#332](https://github.com/tuwien-csd/damap-backend/pull/332).

### Template

- HE terminology section: removed the link above the table and the "EOSC" row [#336](https://github.com/tuwien-csd/damap-backend/pull/336).
- Fixed broken header column text "dataset ID" in FWF template dataset tables [#344](https://github.com/tuwien-csd/damap-backend/pull/344).
- Assigned more space to each "dataset ID" column so the "dataset" is not split and stays in one line [#344](https://github.com/tuwien-csd/damap-backend/pull/344).
- Added cover pages to each template, explaining what do to after exporting to word. This covers replacing placeholders, rewriting automatically generated text and deletions [#345](https://github.com/tuwien-csd/damap-backend/pull/345).

### Resource Files

- Added FWF resource file; the only difference from SE is the following from under "personal.avail": (see section II.1) vs (see section 1a) [#343](https://github.com/tuwien-csd/damap-backend/pull/343).
- Removed mentions of the storage table from resource files, since there is no such table [#347](https://github.com/tuwien-csd/damap-backend/pull/347). 