# TUG deployment (v5)

We run the TU Graz instance **standalone / single-tenant** against our own Postgres and Keycloak,
using our own image (core + TU Graz custom code built together from this repo). Deployment itself is
unchanged from v4; this lists only what's different for v5.

## What changes for v5

1. **New image** — base is already `5.0.0` in the pom, so the build produces the v5 TUG image
   (core + our `at.tugraz.*` code in one). Point the deployment at the new v5 tag.
2. **FITS removed in v5** — the v5 release no longer uses FITS; file analysis is now in-process
   (Tika). The FITS service and any `fits-url` / `rest.fits` config are obsolete.
3. **Update the env** with the values below.

## Config: env vs yaml

We keep as much as possible in **env** (`.env` / compose) — including the project/person service
registration (the classes ship in our image; env just selects them). Only the custom RestClient
wiring and structural Quarkus config stay in the image's `application.yaml`:

- **env:** OIDC, auth claims, DB, CORS, the TU Graz API URLs + credentials, doc export, the
  single-tenant profile, and the project/person services.
- **yaml (image):** the `rest.*` config keys for our TU Graz clients (`rest.projects`,
  `rest.persons`, `rest.tugraz.api.auth`, `rest.orcid.search`) — their URLs come from the env vars
  below — plus Liquibase / Hibernate / cache / swagger. The service block is also present in the
  yaml, commented, as a fallback.

## Env (`.env`)

```
# Single-tenant — leave empty (do NOT set multitenant)
DAMAP_QUARKUS_PROFILE=

# Frontend / CORS (no trailing slash)
DAMAP_QUARKUS_HTTP_CORS_ORIGINS=https://<frontend-host>

# OIDC (our Keycloak)
DAMAP_QUARKUS_OIDC_AUTH_SERVER_URL=https://<keycloak>/realms/<realm>
DAMAP_QUARKUS_OIDC_CLIENT_ID=<client>
DAMAP_QUARKUS_OIDC_TOKEN_ISSUER=https://<keycloak>/realms/<realm>
DAMAP_AUTH_USER_ROLES_CLAIM_PATH=resource_access/<client>/roles   # admin is a CLIENT role
DAMAP_AUTH_USER_ID_CLAIM=personID                                 # v5 key (was "user")
DAMAP_AUTH_SCOPE="openid profile email offline_access microprofile-jwt roles personID"

# Database (existing Postgres)
DAMAP_DB_HOST=<host>
DAMAP_DB_NAME=<name>
DAMAP_DB_USERNAME=<user>
DAMAP_DB_PASSWORD=<password>

# Project & person services registered via env; classes are in our image
DAMAP_TENANT_AWARE_PROJECT_SERVICE=tugraz
DAMAP_TENANT_AWARE_PERSON_SERVICES_0__DISPLAY_TEXT=University
DAMAP_TENANT_AWARE_PERSON_SERVICES_0__QUERY_VALUE=UNIVERSITY
DAMAP_TENANT_AWARE_PERSON_SERVICES_0__CLASS_NAME=at.tugraz.damap.rest.persons.TUGrazPersonServiceImpl
DAMAP_TENANT_AWARE_PERSON_SERVICES_1__DISPLAY_TEXT=ORCID
DAMAP_TENANT_AWARE_PERSON_SERVICES_1__QUERY_VALUE=ORCID
DAMAP_TENANT_AWARE_PERSON_SERVICES_1__CLASS_NAME=org.damap.base.integration.orcid.ORCIDPersonServiceImpl

# TU Graz person/project API (our custom integration)
DAMAP_PERSONS_URL=https://api.tugraz.at/base/people
DAMAP_PROJECTS_URL=https://api.tugraz.at/base/research-projects
DAMAP_TUGRAZ_API_AUTH_URL=https://auth.tugraz.at/.../protocol/openid-connect/token
DAMAP_TUGRAZ_API_AUTH_CLIENT=client
DAMAP_TUGRAZ_API_AUTH_SECRET=secret         

# Document export
DAMAP_REST_GOTENBERG_MP_REST_URL=http://<gotenberg>:3000
```

## How we tested it (locally)

Validated end-to-end in `quarkus:dev` against the TU Graz **demo** API:

- Set the env vars above to demo values (`api-demo.tugraz.at`, Keycloak `auth-demo.tugraz.at`,
  client `researchproject-demo` + its secret), then started dev mode.
- Person and project search returned real data from the demo API.
- Confirmed env-only service registration works (log: `PersonService selected ... TUGrazPersonServiceImpl`).

Two flags are **local-dev only** :
`-Dquarkus.class-loading.reloadable-artifacts=org.damap:base` (dev classloader workaround) and
`-Ddamap.auth.user-roles-claim-path=realm_access/roles` (the local sample Keycloak uses realm roles;
prod uses the client-roles path above).

## Database

Point at our existing Postgres (the v4 DB carries over). **Back it up before the first v5 boot** —
Liquibase applies the v5 schema changes on start. Existing DMPs are kept; the new v5 admin tables
(translations, recommended repos, instance config) are created empty.

## First start

After the first deploy the instance comes up **locked** — that's a v5 thing: non-admins land on a
lock page until someone turns it on. So we need a real user with the `Damap Admin` client role in
Keycloak first. That person logs in at `/admin`, sets the recommended repository (the TU Graz
Repository, `repository.tugraz.at`), the colours, images and UI texts, and then switches **public
availability on**.

It's a one-time setup  everything here is stored in the database, so it stays across restarts and
redeploys as long as the DB is persistent. It does not carry over from the test instance, so it
has to be done again on prod.

## Optional features (off by default)

- **FAIR/DMP evaluation** (OSTrails): set `evaluation/mp-rest/url` to enable. It sends the full DMP
  to an external evaluation service, so clear the data-protection side first. 
- **Elsevier Pure**: not used at TUG (we use our own TU Graz API).
