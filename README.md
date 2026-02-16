# Campus Closet 
## A Student Thrifting Mobile Platform

Campus Closet is a mobile application designed exclusively for university students to buy, sell, and exchange clothing within their campus community. The platform promotes affordability, sustainability, and safe peer-to-peer transactions within a verified student network.

This project is being developed as part of a Software Engineering course and follows an iterative sprint-based development process.

### Project Goals
- Create a secure, student-only marketplace
- Ensure authenticated access using university email verification
- Provide a structured system for listing and managing items
- Maintain reliable data storage and user management
- Apply software engineering principles including UML design, database modeling, and system implementation

### Tech Stack
- Platform: Native Android
- Language: Kotlin
- Database: SQLite (via Room ORM)
- Session Management: SharedPreferences
- Architecture: Activity-based Android structure
- Version Control: GitHub

## Core Features (Current Implementation)
- User Management
- User Registration (restricted to @student.gsu.edu emails)
- Secure Login Authentication
- Persistent Session Handling
- Logout Functionality
- Role support (User / Admin – extendable)
- Database System
- Room-based SQLite database
- Structured User entity
- DAO queries for insert, validation, and retrieval
- Verified persistent storage

### Database Overview

Table: Users

Attribute	Type	Description
userId	Int (Primary Key)	Auto-generated unique ID
name	String	Student name
email	String	University email (unique)
password	String	Stored password
role	String	User role

Database Management System: SQLite (implemented using Room)

### System Design

The project includes:

Context Diagram
Activity Diagrams
Use Case Diagrams
Database Schema Design
Sprint Planning & Scheduling

All diagrams follow standard UML conventions.

## Running the Application

- Clone the repository
- Open in Android Studio
- Allow Gradle to sync
- Run on an emulator or Android device
- No external configuration required.

## Development Approach

The project follows a sprint-based model:

- Sprint 1: System planning and initial design

- Sprint 2: Database and user management implementation

Future Sprints: Marketplace features, item listings, transaction flows

👥 Team

Software Engineering Project
Group: 2