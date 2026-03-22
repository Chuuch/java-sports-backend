# Sportly Platform — Sports Backend

Enterprise-style **REST API** for sports data: user authentication, matches, live listings, and score updates. Built with **Java 21** and **Spring Boot 4**, using a **hexagonal (ports & adapters)** layout, **PostgreSQL**, **Redis** caching, **Apache Kafka** for domain events, and **JWT**-based security.

## Features

- **Auth** — Register and login; JWT access tokens; BCrypt password hashing; role-based access (`USER`, `ADMIN`).
- **Matches** — Create matches (admin), list live matches (cached), update scores (admin); domain rules enforced in the model layer.
- **Persistence** — JPA entities, **Flyway** migrations, MapStruct mappers between domain and infrastructure.
- **Messaging** — Kafka topics for score updates and user registration events.
- **Email** — Welcome mail via **Resend** (configurable).
- **Observability** — Actuator (health, Prometheus in production-oriented config), Micrometer metrics, optional Zipkin tracing in `prod`.
- **Deployment** — **Docker** multi-stage build, **Docker Compose** for Postgres + app, **Kubernetes** manifests under `k8s/`.

## Tech stack

| Area | Choice |
|------|--------|
| Runtime | Java 21 |
| Framework | Spring Boot 4.0.x, Spring Security, Spring Data JPA |
| Database | PostgreSQL 15+ |
| Migrations | Flyway |
| Cache | Redis (`liveMatches` cache in default setup) |
| Events | Spring Kafka |
| API | REST (`/api/v1/...`) |
| Build | Maven |
| CI | GitHub Actions (`mvn clean verify`, Docker image build on `master`) |

## Requirements

- **JDK 21** and **Maven 3.9+** (or use `./mvnw`)
- **Docker** (optional, for Compose or the CI-style image build)
- For a **full local run** matching the default `dev` profile: **PostgreSQL**, **Redis**, and **Kafka** reachable where your config points (defaults assume localhost services)

## Quick start — build & test

```bash
./mvnw clean verify
```

Tests use the `test` profile (in-memory H2, no real Redis/Kafka). MapStruct processors are configured in the POM; generated mapper implementations live under `target/generated-sources/annotations` after compile.

## Run locally (Maven)

1. Start **PostgreSQL** and create a database (default JDBC URL: `jdbc:postgresql://localhost:5432/sportly`, user/password `postgres` unless overridden).
2. Start **Redis** and **Kafka** if you run with the default `dev` profile and full infrastructure beans enabled.
3. Set at least:

   | Variable / property | Purpose |
   |---------------------|---------|
   | `APP_SECURITY_JWT_SECRET` | HS256 signing secret (use a long random string, e.g. 32+ bytes) |
   | `APP_SECURITY_JWT_EXPIRATION` | Access token TTL in ms (optional; has defaults in some profiles) |
   | `APP_EMAIL_FROM` | From address for outbound mail |
   | `APP_EMAIL_RESEND_API_KEY` | Resend API key (if using email) |

   Spring maps `APP_SECURITY_JWT_SECRET` to `app.security.jwt.secret` (see `JwtService` and `application-prod.yaml` for the `prod` shape).

4. Run:

```bash
./mvnw spring-boot:run
```

API base URL: `http://localhost:8080`.

## Docker Compose

```bash
docker compose up --build
```

The bundled `docker-compose.yaml` starts **Postgres** and builds the **app** image. Set JWT and email env vars using names Spring understands (for example **`APP_SECURITY_JWT_SECRET`** for `app.security.jwt.secret`), or add matching placeholders in your YAML. Extend the file with **Redis** and **Kafka** (and any secrets) if you run the full stack in containers.

## API overview

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/api/v1/auth/register` | Public | Register user |
| `POST` | `/api/v1/auth/login` | Public | Login |
| `GET` | `/api/v1/matches/live` | Authenticated | Live matches |
| `POST` | `/api/v1/matches` | `ADMIN` | Create match |
| `PATCH` | `/api/v1/matches/{id}/score` | `ADMIN` | Update score |

Actuator (e.g. health) is available under `/actuator` per your security rules.

## Configuration profiles

- **`dev`** — Default in `application.yaml`; PostgreSQL + Flyway; tune `DB_*` and infrastructure endpoints as needed.
- **`test`** — Used by automated tests (`application-test.yaml`): H2, simplified cache, Kafka/Redis auto-config excluded where appropriate.
- **`prod`** — See `application-prod.yaml` for structured logging, tracing, Prometheus, and externalized secrets (`JWT_SECRET`, DB, Resend, Zipkin, etc.).

## CI/CD

Workflow: `.github/workflows/ci.yml`

- Triggers on **push** and **pull_request** to `master`.
- Runs **`mvn clean verify`**.
- On non-PR runs: logs into **Docker Hub** using repository secrets `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN`, then **builds** and tags the image (`sports-platform:latest` and `sports-platform:<git-sha>`). Add **`docker push`** steps when you want images published to a registry.

## Project layout (high level)

```
src/main/java/com/sports/platform/
├── application/          # Services, ports (interfaces)
├── domain/               # Models, domain events, exceptions
├── http/                 # REST controllers, DTOs
└── infrastructure/       # JPA, Kafka, Redis, security, email, config
src/main/resources/db/migration/   # Flyway SQL
k8s/                               # Kubernetes Deployment / Service / ConfigMap
```

## License

See the `pom.xml` `<licenses>` section or add a `LICENSE` file when you choose a license.
