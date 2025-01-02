# Sprache APP Backend ⚫🔴🟡

Welcome to the backend of **Sprache APP**, a German language learning platform designed to enhance your learning journey through flashcards, AI-driven tools, and various interactive features! 🚀

---

## 📋 Features

- **User Authentication**: Secure login and registration with JWT-based authentication. 🔒
- **Flashcards Management**: Create, update, delete, and study flashcards with personalized progress tracking. 🃏
- **Redis Caching**: Accelerate performance with optimized caching for frequently accessed data. ⚡
- **AI Integration**: Leverages Llama 3.7B via Groq API to generate intelligent responses. 🤖
- **Periodic Tasks**: AWS Lambda functions to handle scheduled database updates. 📆
- **SSL Security**: Full HTTPS support through Nginx for secure data transmission. 🔐

---

## 🏗️ Architecture Overview

The backend is built using **Spring Boot** and follows a robust architecture:

- **Frontend**: Deployed on [Vercel](https://vercel.com), communicates via REST APIs.
- **Backend**: Hosted on AWS EC2, managed by Jenkins for CI/CD, and runs inside Docker containers.
- **Database**: PostgreSQL for persistent storage, augmented with Redis for caching.
- **Nginx**: Handles proxying and SSL termination.
- **AI**: Connects to Groq API for AI-driven functionalities.
- **Task Scheduling**: AWS Lambda ensures automated updates.

---

## 🔧 Technologies Used

- **Language**: Java 17 ☕
- **Framework**: Spring Boot 🌱
- **ORM**: JPA with Hibernate 🛠️
- **Database**: PostgreSQL 🐘
- **Cache**: Redis 🧰
- **Containerization**: Docker 🐳
- **API Security**: JWT via Spring Security 🔑
- **CI/CD**: Jenkins 🔄
- **Reverse Proxy**: Nginx 🌐
- **Cloud**: AWS (EC2, Lambda, ElastiCache) ☁️
- **AI Integration**: Groq API (Llama 3.7B) 🤖

---

💬 Contact
For questions or feedback, reach out to juand.diaza@gmail.com 📩.
---
