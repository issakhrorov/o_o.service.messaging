# Kotlin Spring Chat App

A real-time chat application built with **Kotlin** and **Spring Boot**, using **WebSocket** for live messaging and **MongoDB** for persistent chat history and user data. It features a **custom WebSocket connection interceptor** for authenticating users before upgrading to a WebSocket session.

---

## 🚀 Features

- **Real-time Chat** using WebSocket
- **Custom WebSocket Interceptor** to authenticate and track user sessions
- **MongoDB** as a document-based database to store messages, conversations, and user data
- Built entirely in **Kotlin** using Spring Boot's WebSocket and Mongo integrations

---

## 🧱 Tech Stack

- Kotlin
- Spring Boot 3
- Spring WebSocket
- MongoDB
- Spring Data MongoDB

---

## 🔗 WebSocket Endpoint

| Endpoint          | Description               |
|-------------------|---------------------------|
| `/ws`             | WebSocket chat endpoint   |

---

## 🔐 WebSocket Interceptor

Before allowing a WebSocket connection, a **custom interceptor** verifies the user's identity via token/session and binds the user to their WebSocket session.

- Intercepts the handshake request
- Extracts and validates user token
- Associates WebSocket session with authenticated user

---

## 🧪 Project Structure
