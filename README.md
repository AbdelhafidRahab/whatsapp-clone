# WhatsApp Clone - Full-Stack Application

This repository contains the source code for a full-stack WhatsApp clone built with a modern, professional technology stack. This project demonstrates a complete, real-time chat application from the database to the user interface, incorporating best practices in security, API design, and frontend architecture.

## 🚀 Features

*   **Real-Time Messaging:** Instantaneous one-on-one chat using WebSockets and the STOMP protocol.
*   **Secure Authentication:** User management and authentication handled by **Keycloak** using an OAuth2/OIDC flow with JWTs.
*   **User & Chat Management:** RESTful API for discovering users, creating chats, and retrieving chat history.
*   **Media Uploads:** Functionality to send and receive image/media messages.
*   **API Documentation:** Interactive API documentation provided by **SpringDoc (Swagger UI)**.
*   **Production-Ready Monitoring:** Application health and metrics exposed via **Spring Boot Actuator**.

## 💻 Tech Stack

### Backend
*   **Java 21**
*   **Spring Boot 3.x**
    *   Spring Web
    *   Spring Data JPA
    *   Spring Security (OAuth2 Resource Server)
    *   Spring WebSocket (STOMP)
*   **PostgreSQL:** Database for storing application data.
*   **Flyway:** For database schema migrations.
*   **Keycloak:** For Identity and Access Management (IAM).
*   **Maven:** For dependency management.

### Frontend (Coming Soon)
*   **Angular 19**
*   **TypeScript**
*   **Tailwind CSS**
*   **SockJS & StompJS:** For real-time WebSocket communication.

### Environment
*   **Docker & Docker Compose:** For orchestrating the development environment (PostgreSQL & Keycloak).

## ⚙️ Getting Started

### Prerequisites

*   Java 21 (or higher)
*   Docker & Docker Compose
*   Node.js and npm (for the frontend)
*   Angular CLI v19

### 1. Setup the Environment

Clone the repository and then start the required services using Docker Compose.

# Clone the repository
git clone https://github.com/AbdelhafidRahab/whatsapp-clone.git
cd whatsapp-clone

# Start the PostgreSQL database and Keycloak server
docker compose up -d

### 2. Configure Keycloak

1.  Navigate to the Keycloak admin console at `http://localhost:9090`.
2.  Log in with the credentials `admin` / `admin`.
3.  Create a new realm named `whatsapp-clone`.
4.  Create a new client for the Angular application.

### 3. Run the Backend

1.  Navigate to the `backend` directory.
2.  Create a `.env` file with your database credentials.
3.  Run the Spring Boot application using your IDE or Maven.

# From the /backend directory
./mvnw spring-boot:run

The backend will be available at `http://localhost:8080`.

### 4. Run the Frontend (Coming Soon)



This project was built for educational purposes to demonstrate modern full-stack development techniques.
