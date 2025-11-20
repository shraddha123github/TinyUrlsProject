# TinyLink - Mongo Backend + JWT

This archive contains:
- tinylink-backend (Spring Boot, MongoDB, JWT auth)
- tinylink-frontend (React + Vite + MUI)

Included assignment PDF: /mnt/data/Take-Home Assignment_ TinyLink (1) (2).pdf

## Quickstart (backend)

1. Start MongoDB locally (example with Docker):
   docker run --name tinylink-mongo -p 27017:27017 -d mongo:6

2. Configure `src/main/resources/application.properties` if needed.

3. Run backend:
   cd tinylink-backend
   mvn spring-boot:run

## Quickstart (frontend)

cd tinylink-frontend
npm install
npm run dev

The frontend expects backend at http://localhost:8080 by default.
