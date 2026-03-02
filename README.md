# Library Management System

A backend project demonstrating clean architecture, JPA entity relationships, and REST API design.  
The project is planned to evolve into a full-stack application with PostgreSQL, Docker, and frontend integration.

## 🚀 Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.5.11
- **Database:** H2 (in-memory), PostgreSQL planned
- **ORM:** Spring Data JPA + Hibernate
- **Tools:** Gradle, Lombok, JUnit 5

---

## 🎗 Architecture

The backend follows layered architecture:

`Controller → Service → Repository → Database`

Key concepts demonstrated:

- Entity relationships (OneToMany / ManyToOne)
- Lazy loading
- Persistence context
- DTO mapping for API data transfer
- Database migration (Flyway planned)
- RESTful API design

---

## 💶 Project Structure

```
library/
├── backend/     ← Spring Boot backend
├── frontend/    ← planned
├── docker/      ← planned
├── k8s/         ← planned
└── README.md
```

---

## ⚙️ Setup & Run

### Clone repository

```bash
git clone https://github.com/apaseka/library_app.git
cd library/backend
```

### Run with Gradle

```bash
./gradlew bootRun
```

Application runs on: http://localhost:8080

### H2 Console (for development)

http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:mem:library`

---

## 📌 Current Progress

- Spring Boot project created (Java 21)
- H2 in-memory database configured
- Book entity & BookRepository created
- BookController implemented

---

## 🌎 Upcoming Plans

- Add Author entity with OneToMany relationship to Book
- Introduce Service layer for business logic
- Implement DTOs for API data transfer
- Switch from H2 to PostgreSQL
- Docker & containerized deployment
- Frontend integration
- Kubernetes deployment

---

## 🗭 API Examples

**Add a Book (POST)**

```bash
POST http://localhost:8080/books
Content-Type: application/json

{
  "title": "Clean Code",
  "author": "Robert Martin",
  "year": 2008
}
```

**Get All Books (GET)**

```bash
GET http://localhost:8080/books
```

Example Response:

```json
[
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert Martin",
    "year": 2008
  }
]
```

---

## 🤏 Purpose

This project demonstrates backend development skills with Spring Boot, proper layered architecture, JPA, and production-oriented practices.  
It will continue to evolve into a full-stack system with PostgreSQL, Docker, and frontend integration.

---

## Author

Oleksandr