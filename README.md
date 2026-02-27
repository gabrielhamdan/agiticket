# AgiTicket

REST API for ticket lifecycle management with role-based access control.

The project models a helpdesk domain with tickets, comments and user roles, focusing on business rule enforcement, stateless authentication and clean layered architecture using Spring Boot.

> API responses are localized in Portuguese (pt-BR).

---

## Domain Overview

The system is centered around three main entities:

- **User**
- **Ticket**
- **Comment**

### Roles

- `ADMIN`
- `TECH`
- `USER`

---

## Business Rules

### Tickets

- Any authenticated user can create tickets.
- Tickets support status and priority.
- Any authorized role can update ticket status.
  - `USER` can only set status to `CLOSED`
  - Only `ADMIN` can reopen a ticket

### Comments

- Only the comment author can edit their comment.
- `ADMIN` can delete any comment.
- Timestamps (`createdAt`, `updatedAt`) are maintained for tickets and comments.

---

## Security

- Stateless authentication using JWT.
- Custom `OncePerRequestFilter` for token validation.
- Role-based access control using:
  - Endpoint-level authorization with @PreAuthorize
  - Domain-level rule enforcement in the service layer
- Password encryption with BCrypt.

---

## Architecture

Layered structure:

```
api/
├─ docs/
├─ exception/
├─ response/
├─ security/
│  ├─ jwt/
controllers/
domain/
├─ comment/
├─ ticket/
├─ user/
│  ├─ auth/
│  ├─ permission/

```

### Design Decisions

- Clear separation between domain entities and DTOs.
- Pagination abstraction via a custom `ApiPaginationDto`.
- Centralized exception handling.
- Business rules enforced in the service layer.
- Physical deletes for tickets and comments.
- User lifecycle controlled via `enabled` flag.
- OpenAPI documentation available via Swagger UI.

---

## Database

- PostgreSQL
- Flyway for schema versioning and migration control

---

## Technical Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT
- Flyway
- PostgreSQL
- Lombok

---

## Deployment

The application is deployed on Render using Docker.

- **Cloud Platform:** Render
- **Database:** Managed PostgreSQL (Render)
- **Containerization:** Docker (multi-stage build)
- **Environment Variables:** Configured via Render dashboard
- **Migrations:** Flyway executed automatically on startup

### Live API

**Base URL:** https://agiticket.onrender.com

### [API Documentation (Swagger UI)](https://agiticket.onrender.com/api/v1/swagger-ui/index.html)

---

## Project Goal

This project was built to demonstrate:

- Role-based access control with real business constraints
- Clean layered architecture
- Stateless authentication with JWT
- Domain-level rule enforcement in the service layer
- Structured exception handling