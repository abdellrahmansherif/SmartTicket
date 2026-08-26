# SmartTicket Backend API

> A Spring Boot ticketing backend engineered around a modular monolith architecture, transactional seat reservations, idempotent payment integration, and strict domain boundaries.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=flat-square&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=flat-square&logo=docker)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)

---

## Overview

**SmartTicket** is a high-reliability backend API designed for event ticket sales, venue seating management, and sports ticketing. Instead of acting as a generic CRUD service, SmartTicket addresses core distributed system challenges present in ticketing platforms: **race conditions during high-demand seat holds**, **idempotent payment processing**, **strict state machine transitions**, and **modular domain boundary enforcement**.

---

## Key Engineering Highlights

* **Modular Monolith Architecture:** Clean package boundaries using Spring Modulith patterns; prevents cross-module database access.
* **Concurrency-Safe Seat Holds:** Database-level isolation preventing double booking under concurrent requests.
* **Idempotent Webhook Processing:** Deterministic handling of third-party payment provider webhooks with replay protection.
* **Domain State Machine Verification:** Strict domain-driven validation preventing illegal transitions (e.g., `EXPIRED` -> `CONFIRMED`).
* **Real-Database Integration Testing:** Comprehensive testing suite powered by JUnit 5 and Testcontainers (PostgreSQL).

---

## Features

### ✅ Implemented
* JWT-based Authentication and Role-Based Access Control (`ROLE_ADMIN`, `ROLE_CUSTOMER`).
* Venue, Section, and Seat structure modeling with dynamic pricing tiers.
* Event, Football Match, Team, and Competition domain catalog.
* OpenAPI 3.0 / Swagger UI documentation integration.
* Centralized exception handling with structured `ProblemDetail` responses.

### 🚧 In Progress
* Transactional seat hold logic using PostgreSQL conditional updates.
* Background reservation expiration worker with automated inventory release.
* Third-party payment gateway integration and signed webhook handler.

### 📌 Planned / Roadmap
* Idempotent request registry using client-provided `Idempotency-Key` headers.
* Architecture constraint enforcement via ArchUnit tests.
* Structural logging with tracing correlation IDs (`traceId`, `spanId`).
* Rate limiting on sensitive endpoints via Bucket4j.

---

## Architecture

SmartTicket uses a **Modular Monolith** pattern. All bounded contexts reside inside a single deployable unit while maintaining absolute boundary isolation. Inter-module communication occurs strictly via explicit public APIs (`.api` packages) rather than internal persistence layers or direct entity sharing.
