# ScrumWeb

REST API for managing Scrum projects: product backlogs, epics, user stories,
sprints and tasks. Built with Spring Boot 3, Spring Security (JWT), Spring Data
JPA, Flyway and MySQL.

## Architecture

The code is organised by feature rather than by technical layer:

```
src/main/java/org/example/
├── common/      # exceptions + global ProblemDetail error handling
├── config/      # security, CORS and filter chain configuration
├── security/    # JWT generation/validation, auth entry points
├── user/        # accounts, authentication and roles
├── project/     # projects, membership and project-scoped authorization
├── backlog/     # product backlogs, epics and user stories
└── sprint/      # sprint backlogs and tasks
```

Key rules:

- **Project-scoped roles.** Users have a global `RoleType`
  (`PRODUCT_OWNER`, `SCRUM_MASTER`, `DEVELOPER`), and each project assigns a
  role to every member through `ProjectMember`. All authorization is delegated
  to `ProjectAccessService`; a non-member never sees another project's data.
- **Thin controllers.** Controllers only translate HTTP to service calls and
  never access repositories or entities directly.
- **Consistent errors.** `ApiExceptionHandler` maps exceptions to RFC 7807
  `ProblemDetail` responses (`400`, `401`, `403`, `404`, `405`, `409`, `500`).
- **Validated schema.** Hibernate runs with `ddl-auto=validate`; the schema is
  owned by Flyway (`src/main/resources/db/migration`).

## Requirements

- Java 17+
- Maven (the wrapper `./mvnw` is included)
- MySQL 8 (tests use in-memory H2)

## Configuration

All environment-specific values are supplied through environment variables.
See `.env.example`.

| Variable | Purpose |
| --- | --- |
| `DB_URL` | JDBC URL of the MySQL database |
| `DB_USERNAME` / `DB_PASSWORD` | Database credentials |
| `JWT_SECRET` | HMAC signing key, **at least 32 bytes** |
| `JWT_EXPIRATION` | Token lifetime in milliseconds (default 24h) |
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed origins |

Example:

```bash
export DB_URL='jdbc:mysql://localhost:3306/scrumweb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true'
export DB_USERNAME=root
export DB_PASSWORD=secret
export JWT_SECRET="$(openssl rand -base64 48)"
./mvnw spring-boot:run
```

Flyway creates the schema on first start. Health check:
`GET /actuator/health`.

## Tests

```bash
./mvnw clean test
```

The suite covers the service layer with Mockito plus an end-to-end MockMvc flow
(register/login, project creation, backlog, sprint, task, status update and
project isolation) against H2.

## API overview

All routes are versioned under `/api/v1` and require
`Authorization: Bearer <token>` except registration and login.

### Authentication and users

| Method | Path | Description |
| --- | --- | --- |
| `POST` | `/auth/register` | Create an account (always `DEVELOPER`) |
| `POST` | `/auth/login` | Obtain a JWT |
| `GET` | `/users/me` | Current user profile |

### Projects and members

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/projects` | Projects the current user belongs to |
| `POST` | `/projects` | Create a project (creator becomes product owner) |
| `GET` | `/projects/{projectId}` | Project details |
| `PUT` | `/projects/{projectId}` | Update a project (product owner) |
| `GET` | `/projects/{projectId}/members` | List members |
| `POST` | `/projects/{projectId}/members` | Add a member (product owner) |
| `DELETE` | `/projects/{projectId}/members/{userId}` | Remove a member (product owner) |

### Product backlog, epics and user stories

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/projects/{projectId}/backlog` | Backlog of a project |
| `GET` | `/product-backlogs/{backlogId}` | Backlog by id |
| `GET` | `/product-backlogs/{backlogId}/user-stories` | Stories in a backlog |
| `POST` | `/product-backlogs/{backlogId}/user-stories` | Create a story |
| `GET` | `/product-backlogs/{backlogId}/epics` | Epics in a backlog |
| `POST` | `/product-backlogs/{backlogId}/epics` | Create an epic |
| `GET` / `PUT` / `DELETE` | `/epics/{epicId}` | Read/update/delete an epic |
| `GET` | `/epics/{epicId}/user-stories` | Stories linked to an epic |
| `POST` / `DELETE` | `/epics/{epicId}/user-stories/{userStoryId}` | Link/unlink a story |
| `GET` / `PUT` / `DELETE` | `/user-stories/{userStoryId}` | Read/update/delete a story |
| `PATCH` | `/user-stories/{userStoryId}/priority` | Change story priority |

### Sprints and tasks

| Method | Path | Description |
| --- | --- | --- |
| `GET` / `POST` | `/projects/{projectId}/sprints` | List/create sprints |
| `GET` / `PUT` / `DELETE` | `/sprints/{sprintId}` | Read/update/delete a sprint |
| `GET` | `/sprints/{sprintId}/user-stories` | Stories in a sprint |
| `POST` / `DELETE` | `/sprints/{sprintId}/user-stories[/{userStoryId}]` | Add/remove a story |
| `GET` / `POST` | `/sprints/{sprintId}/tasks` | List/create tasks |
| `DELETE` | `/sprints/{sprintId}/tasks/{taskId}` | Remove a task |
| `GET` / `PUT` | `/tasks/{taskId}` | Read/update a task |
| `PATCH` | `/tasks/{taskId}/status` | Change task status |

## License

Internal project.
