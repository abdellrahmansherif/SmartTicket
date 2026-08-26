# SmartTicket

A Spring Boot ticketing backend built as a modular monolith, with concurrency-safe seat holds, transactional seat inventory, and idempotent order/payment processing for events and football matches.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Spring Modulith](https://img.shields.io/badge/Spring%20Modulith-dependency-lightgrey)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

## Overview

SmartTicket is a backend for reserving and selling seats to events — concerts, general admission events, and football matches with home/away teams and competitions. The project is organized as a **modular monolith**: modules (`identity`, `venue`, `event`, `inventory`, `reservation`, `order`, `payment`) each expose a narrow `api` package and hide their persistence and domain logic under `internal`, so other modules depend on facades (`EventFacade`, `ReservationFacade`, `OrderFacade`, `EventSeatFacade`) instead of each other's repositories.

The core engineering problem the project takes on is **seat concurrency**: making sure two customers can never both successfully hold the same seat, and that a temporarily held seat is released automatically if the customer doesn't complete checkout in time.

## Key Engineering Highlights

- Modular monolith with explicit `api` / `internal` boundaries and facade-based cross-module calls
- Concurrency-safe seat holds via PostgreSQL pessimistic row locking (deadlock-avoided with a deterministic lock order)
- Idempotent order and payment state transitions under duplicate webhook delivery
- JWT-based stateless authentication with role-based and method-level authorization
- Scheduled expiration of unpaid seat holds

## Features

### Implemented
- Venue → Section → Seat catalog with admin-only mutation endpoints
- Event catalog, categories, and football-specific domain (teams, competitions, matches)
- Event-seat inventory with per-event pricing and status (`AVAILABLE` / `HELD` / `RESERVED` / `SOLD`)
- Seat holds with a 10-minute expiry, enforced by pessimistic locking, not application-level checks
- A scheduled job (`@Scheduled`, 30s interval) that releases expired holds back to `AVAILABLE`
- Reservation → Order → Payment flow, with ownership checks (a customer can't order or view another customer's reservation)
- Idempotent payment webhook handling (duplicate deliveries are absorbed, not reprocessed)
- JWT authentication (RSA-signed, `oauth2ResourceServer` resource-server style validation), BCrypt password hashing, `ADMIN` / `CUSTOMER` roles
- Global exception handling (`@RestControllerAdvice`) mapping domain exceptions to a structured JSON error body
- OpenAPI/Swagger UI with bearer-token auth wired in
- `docker-compose.yml` for a local PostgreSQL instance

### In Progress / Partial
- **Payment webhook signature verification** — the verifier currently accepts any non-blank signature header; it does not yet validate an HMAC against a webhook secret
- **Flyway migrations** — Flyway is on the classpath and one migration exists (`users` table), but the schema for reservations, orders, payments, and inventory is not yet migration-managed
- **Redis-backed distributed seat locking** — `Redis` is a dependency and a `SeatLockManager` abstraction exists, but the Redis implementation is currently a stub (`tryLock` always returns `false`); seat-holding safety today comes entirely from PostgreSQL pessimistic locks, not Redis

### Planned
- Ticket issuance after a paid order (no `Ticket` entity exists yet — see [Data Model](#data-model))
- Idempotency-key support on `POST /api/orders` and `POST /api/payments`
- Automated tests beyond the default Spring context-load test (unit, integration, concurrency, security)
- Consistent API versioning and resource naming across modules
- Payment amount derived server-side from the order rather than accepted from the client

## Architecture

### Why a Modular Monolith
A single deployable unit is the right call here: the domain is one connected purchase workflow (reservation → order → payment) that benefits from local transactions and simple deployment, and the project doesn't have the team size or independent-scaling needs that would justify the operational cost of microservices. The module boundaries below exist to keep that option open later without paying for it now.

### Module Boundaries
```
Clients
   │
   ▼
Spring Security (JWT / OAuth2 Resource Server)
   │
   ▼
identity → venue → event → inventory → reservation → order → payment
   │
   ▼
PostgreSQL
```
Each module exposes a small `api` package (facades and result records) and keeps entities, repositories, and controllers under `internal`. For example, `OrderService` depends on `ReservationFacade` — never on `ReservationRepository` — and `PaymentService` depends on `OrderFacade` the same way. This is enforced by convention today (Spring Modulith is on the classpath but there's no ArchUnit/Modulith test yet verifying it — see [Roadmap](#roadmap)).

### Module Dependency Direction
```
venue  ─┐
        ├─▶ event ─▶ inventory ─▶ reservation ─▶ order ─▶ payment
identity┘
```

## Project Structure
```
src/main/java/com/smartticket
├── identity        # auth, JWT, roles, users
├── venue            # venues, sections, seats
├── event            # events, categories, teams, competitions, football matches
├── inventory        # event-seat availability, holds, expiration
├── reservation       # reservation + reservation items
├── order             # orders
├── payment           # payments, gateway, webhook
└── common            # shared exceptions, global error handling
```
Each module follows `api/` (public facade) + `internal/{domain,application,persistence,web}` (everything else).

## Domain Model

The implemented schema matches the ERD for `User`, `Category`, `Venue → Section → Seat`, `Event`, `EventSeat`, `FootballMatch (+ Team, Competition)`, `Reservation → ReservationItem`. Two differences from the ERD as currently drawn:

- **`Order` and `Payment` exist in code but are not yet in the ERD.** `Order` links to `Reservation` (`reservation_id`, unique `order_number`, `status`, `total_amount`); `Payment` links to `Order` (`order_id`, `transaction_id`, `amount`, `status`).
- **`Ticket` is in the ERD but not yet in code.** The purchase flow today ends at `Order.status = PAID`; no `Ticket` entity, table, or endpoint exists yet.

![SmartTicket ERD](docs/images/smartticket-erd.png)

## Core Workflows

### Seat Reservation Flow
1. Client requests a hold on one or more `EventSeat`s for an event.
2. `EventSeatFacade.holdSeats` locks the requested rows (`SELECT ... FOR UPDATE`, ordered by seat ID to avoid deadlocks), lazily releases any already-expired holds it encounters, and rejects the request if any seat isn't `AVAILABLE`.
3. Seats are marked `HELD` with a 10-minute `heldUntil`, and a `Reservation` + `ReservationItem`s are created in the same transaction.
4. A scheduled job releases seats whose hold has expired back to `AVAILABLE` every 30 seconds.

### Order & Payment Flow
1. `POST /api/orders` creates an `Order` from a `PENDING`, non-expired reservation owned by the current user (rejects duplicate orders per reservation).
2. `POST /api/payments` creates a `Payment` and calls the payment gateway (`StripePaymentGateway`, currently a mock returning a fabricated transaction ID and checkout URL).
3. The gateway's webhook (`POST /api/payments/webhook`) marks the `Payment` `SUCCESS`, which — via `OrderFacade.markAsPaid` — marks the `Order` `PAID`. Both transitions are guarded by pessimistic row locks and return early if already applied, so a webhook delivered multiple times only applies the transition once.

### Ticket Issuance Flow
Not yet implemented — planned as the next step after `Order.PAID`.

## Reservation Concurrency

Two customers cannot both hold seat `A12`: `holdSeats` takes a `PESSIMISTIC_WRITE` lock on every requested `EventSeat` row (ordered by ID, so concurrent multi-seat requests can't deadlock each other), re-checks status inside the lock, and only then flips seats to `HELD`. A losing concurrent request sees the seat as unavailable and fails cleanly with `EventSeatNotAvailableException`. This is a database-correctness solution rather than a distributed lock, and it's sufficient for a single-instance deployment — a Redis-based `SeatLockManager` interface exists for a future multi-instance scenario but isn't implemented.

## Transaction Strategy

`holdSeats`, `reserve`, `markAsPaid`, and webhook handling are each wrapped in a single `@Transactional` boundary that only touches the database — no outbound HTTP calls happen inside a transaction. The one exception worth flagging: `PaymentService.createPayment` calls the payment gateway *inside* a `@Transactional` method, which means a slow or failing gateway call holds a database transaction open longer than it should (see [Roadmap](#roadmap)).

## Security

- Stateless JWT auth via Spring's OAuth2 resource-server support; tokens are RSA-signed with an in-memory keypair generated at startup (note: this means tokens don't survive an application restart, which is fine for local dev but worth knowing).
- `ADMIN` and `CUSTOMER` roles, mapped from a JWT `role` claim to `ROLE_*` authorities.
- Method-level `@PreAuthorize` on admin-only write operations (venues, seats, categories, teams, competitions, football matches, event-seats, order creation restricted to `CUSTOMER`).
- Ownership checks in `OrderService` and `ReservationService` prevent one customer from acting on another's reservation or order.
- `/api/v1/auth/register`, `/api/v1/auth/login`, and Swagger UI are the only endpoints permitted without a token; everything else requires authentication.

## Payment Reliability

Payment state transitions (`Payment.markAsPaid`, `Order.markAsPaid`) are idempotent by construction — both look up their row with a pessimistic write lock and return immediately if already in a terminal state, so a webhook fired five times only moves the order to `PAID` once. What isn't in place yet: **the webhook signature check currently accepts any non-empty signature header rather than verifying an HMAC against a webhook secret**, and the payment amount is taken from the client request rather than derived from the order total — both are flagged in the [Roadmap](#roadmap) as the top priority items before this is demo-safe.

## API Design

Representative endpoints (routes are not yet consistently versioned or named — see Roadmap):

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/v1/auth/register` | Register a user | Public |
| POST | `/api/v1/auth/login` | Log in, receive JWT | Public |
| GET | `/api/events` | Browse events | Authenticated |
| GET | `/api/event-seats/{id}` | Seat availability/status | Authenticated |
| POST | `/api/reserve` | Hold seats | Customer |
| PATCH | `/api/reserve/{id}/cancel` | Release a reservation | Customer |
| POST | `/api/orders` | Create order from reservation | Customer |
| POST | `/api/orders/{id}/cancel` | Cancel a pending order | Customer |
| POST | `/api/payments` | Start payment for an order | Customer |
| POST | `/api/payments/webhook` | Payment provider callback | Provider |
| POST | `/api/admin/football-matches` | Create a football match | Admin |

## API Documentation

Swagger UI is configured with bearer-token auth support: `http://localhost:8080/swagger-ui.html`.

## Technology Stack

- **Language / Runtime:** Java 21
- **Framework:** Spring Boot 4.1 (Web, Security, Data JPA, Validation)
- **Modularity:** Spring Modulith (dependency present)
- **Database:** PostgreSQL 16
- **Migrations:** Flyway (partially applied — see Roadmap)
- **Auth:** Spring Security OAuth2 Resource Server, JWT (RSA), BCrypt
- **Docs:** springdoc-openapi / Swagger UI
- **Media:** Cloudinary (event/team images)
- **Cache/locking dependency:** Redis (not yet wired into the seat-lock path)
- **Containerization:** Docker Compose (PostgreSQL)

## Getting Started

### Prerequisites
- Java 21
- Docker (for PostgreSQL via Compose)

### Environment Variables
Copy `.env.example` to `.env` and fill in real values — do not commit real credentials (see note below).
```
DB_URL, DB_USERNAME, DB_PASSWORD
CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, CLOUDINARY_API_SECRET
REDIS_HOST, REDIS_PORT
POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_PORT
```

> **Before publishing this repository:** `application.properties` currently contains a real database password and real Cloudinary credentials, and `compose.yml` has a default Postgres password checked in. Rotate these credentials and move them to environment-variable placeholders before making the repository public.

### Local Development
```bash
git clone <repo-url>
cd smartticket
docker compose up -d
./mvnw spring-boot:run
```

## Database Migrations

Flyway is configured (`ddl-auto=none`), but only one migration exists (`V1__create_users_table.sql`). Migrations for `venues`, `events`, `event_seats`, `reservations`, `orders`, and `payments` still need to be written before the schema is fully migration-managed.

## Testing

Only the default Spring Boot context-load test currently exists. No unit, integration, concurrency, or security tests are implemented yet — this is the most impactful next investment (see Roadmap).

## Roadmap

**P0 — Correctness & security**
- [ ] Real webhook signature verification (HMAC against a webhook secret, not a non-blank check)
- [ ] Derive payment amount from `Order.totalAmount` server-side instead of trusting the client
- [ ] Rotate and remove committed credentials from `application.properties` / `compose.yml`

**P1 — Professional backend practice**
- [ ] Flyway migrations for all tables
- [ ] Idempotency-key support on `POST /api/orders` and `POST /api/payments`
- [ ] Move the payment-gateway call in `PaymentService.createPayment` outside the database transaction
- [ ] Consistent API versioning and resource naming (`/api/v1/...`, `/api/reservations` instead of `/api/reserve`)

**P2 — Portfolio strength**
- [ ] `Ticket` module: entity, issuance triggered on `Order.PAID`, ticket code/QR
- [ ] Unit, integration (Testcontainers + PostgreSQL), and concurrency tests (concurrent seat-hold race, duplicate-webhook idempotency)
- [ ] ArchUnit or Spring Modulith verification test enforcing the `api`/`internal` boundary

**P3 — Advanced / optional**
- [ ] Finish or remove the Redis `SeatLockManager` (currently a non-functional stub)
- [ ] Rate limiting on `/auth/login` and `/payments`
- [ ] Observability (structured logging, correlation IDs, Actuator)

## What This Project Demonstrates

- Database-level concurrency control for a real race condition (double seat booking)
- Idempotent state machines for payment/order transitions under duplicate delivery
- Module boundaries enforced by facade interfaces rather than shared repository access
- Awareness of what's still missing — the sections above are written from what the code actually does, not what it's meant to eventually do

## License

MIT
